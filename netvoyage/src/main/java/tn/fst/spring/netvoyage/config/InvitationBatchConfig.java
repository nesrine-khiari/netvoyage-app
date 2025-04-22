package tn.fst.spring.netvoyage.config;
import java.util.ArrayList;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.List;
import tn.fst.spring.netvoyage.entities.Entreprise;
import tn.fst.spring.netvoyage.entities.Invitation;
import tn.fst.spring.netvoyage.repositories.EntrepriseRepository;
import tn.fst.spring.netvoyage.services.implementations.EmailService;
import tn.fst.spring.netvoyage.dtos.InvitationItem;
import tn.fst.spring.netvoyage.enums.InvitationStatus;
import tn.fst.spring.netvoyage.repositories.InvitationRepository;

import java.util.UUID;
import java.time.Instant;
import java.io.File;
import java.util.UUID;
import java.time.Instant;
import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.configuration.annotation.StepScope;

@Configuration
@EnableBatchProcessing
public class InvitationBatchConfig {

    private final EntrepriseRepository entrepriseRepository;
    private final EmailService emailService;

    public InvitationBatchConfig(EntrepriseRepository entrepriseRepository,
                                 EmailService emailService) {
        this.entrepriseRepository = entrepriseRepository;
        this.emailService = emailService;
    }

    @Bean
    public Job processInvitationJob(JobRepository jobRepository, Step invitationStep) {
        return new JobBuilder("processInvitationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(invitationStep)
                .build();
    }

    @Bean
    public Step invitationStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               ItemReader<InvitationItem> reader,
                               ItemProcessor<InvitationItem, Invitation> processor,
                               ItemWriter<Invitation> writer) {
        return new StepBuilder("invitationStep", jobRepository)
                .<InvitationItem, Invitation>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<InvitationItem> reader(
            @Value("#{jobParameters['filePath']}") String filePath) {
        return new FlatFileItemReaderBuilder<InvitationItem>()
                .name("invitationItemReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names(new String[]{"nom", "email"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<InvitationItem>() {{
                    setTargetType(InvitationItem.class);
                }})
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<InvitationItem, Invitation> processor(
            @Value("#{jobParameters['entrepriseId']}") Long entrepriseId) {
        return item -> {
            Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
                    .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));

            Invitation invitation = new Invitation();
            invitation.setNomInvite(item.getNom());
            invitation.setEmailInvite(item.getEmail());
            invitation.setToken(UUID.randomUUID().toString());
            invitation.setDateEnvoi(Instant.now());
            invitation.setStatus(InvitationStatus.ENVOYEE);
            invitation.setEntreprise(entreprise);

            return invitation;
        };
    }

    @Bean
    public ItemWriter<Invitation> writer(InvitationRepository invitationRepository, EmailService emailService) {
        return items -> {
            for (Invitation invitation : items) {
                try {
                    String emailExpediteur = (invitation.getEntreprise().getUser() != null)
                            ? invitation.getEntreprise().getUser().getEmail()
                            : null;

                    if (emailExpediteur == null) {
                        throw new RuntimeException("L'entreprise n'a pas d'utilisateur associé avec un email.");
                    }

                    emailService.sendInvitationEmail(
                            invitation.getEmailInvite(),
                            invitation.getToken(),
                            emailExpediteur,
                            invitation.getNomInvite()
                    );
                    invitation.setStatus(InvitationStatus.ENVOYEE);
                } catch (Exception e) {
                    invitation.setStatus(InvitationStatus.EXPIREE);
                    System.err.println("Erreur lors de l'envoi de l'email à " + invitation.getEmailInvite() + ": " + e.getMessage());
                }
            }

            invitationRepository.saveAll(items);
        };
    }

}