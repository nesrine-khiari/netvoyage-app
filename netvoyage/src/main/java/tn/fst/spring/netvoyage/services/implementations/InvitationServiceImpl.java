package tn.fst.spring.netvoyage.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.fst.spring.netvoyage.entities.Entreprise;
import tn.fst.spring.netvoyage.entities.Invitation;
import tn.fst.spring.netvoyage.enums.InvitationStatus;
import tn.fst.spring.netvoyage.repositories.EntrepriseRepository;
import tn.fst.spring.netvoyage.repositories.InvitationRepository;
import tn.fst.spring.netvoyage.services.implementations.EmailService;
import tn.fst.spring.netvoyage.services.interfaces.InvitationService;
import java.io.IOException;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.HashMap;
import java.time.temporal.ChronoUnit;




import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final EmailService emailService;
    private final JobLauncher jobLauncher;
    private final Job processInvitationJob;

    @Override
    public void sendBatchInvitations(MultipartFile file, Long entrepriseId) {
        try {
            // Sauvegarde temporaire du fichier
            String tempDir = System.getProperty("java.io.tmpdir");
            String filePath = tempDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .addLong("entrepriseId", entrepriseId)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(processInvitationJob, jobParameters);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du lancement du batch", e);
        }
    }


    @Override
    public Invitation createInvitation(String nom, String email, Long entrepriseId) {
        // Assuming you have a way to get the authenticated user's entrepriseId
        // Example: SecurityContextHolder.getContext().getAuthentication().getDetails()
        // Validate that entrepriseId matches the authenticated user's enterprise
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));

        // Additional validation if needed
        // if (!isUserAuthorizedForEntreprise(entrepriseId)) {
        //     throw new RuntimeException("Non autorisé pour cette entreprise");
        // }

        Invitation invitation = new Invitation();
        invitation.setNomInvite(nom);
        invitation.setEmailInvite(email);
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setDateEnvoi(Instant.now());
        invitation.setStatus(InvitationStatus.ENVOYEE);
        invitation.setEntreprise(entreprise);

        Invitation savedInvitation = invitationRepository.save(invitation);

        emailService.sendInvitationEmail(
                savedInvitation.getEmailInvite(),
                savedInvitation.getToken(),
                entreprise.getNomEntreprise(),
                savedInvitation.getNomInvite()
        );

        return savedInvitation;
    }

    @Override
    public List<Invitation> getInvitationsByEntrepriseAndStatus(Long entrepriseId, InvitationStatus status) {
        return invitationRepository.findByEntrepriseIdAndStatus(entrepriseId, status);
    }

    @Override
    public Invitation updateInvitation(Long id, Invitation updated) {
        Invitation invitation = invitationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));

        invitation.setEmailInvite(updated.getEmailInvite());
        invitation.setStatus(updated.getStatus());
        return invitationRepository.save(invitation);
    }

    @Override
    public void deleteInvitation(Long id) {
        invitationRepository.deleteById(id);
    }

    @Override
    public Invitation getInvitationById(Long id) {
        return invitationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));
    }

    @Override
    public List<Invitation> getAllInvitations() {
        return invitationRepository.findAll();
    }




    @Transactional
    public Invitation acceptInvitation(String token) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));

        Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);

        if (invitation.getDateEnvoi().isBefore(sevenDaysAgo)) {
            invitation.setStatus(InvitationStatus.EXPIREE);
            invitationRepository.save(invitation);
            throw new RuntimeException("L'invitation a expiré (7 jours de validité)");
        }

        invitation.setStatus(InvitationStatus.ACCEPTEE);
        invitation.setDateAcceptation(Instant.now());

        return invitationRepository.save(invitation);
    }

    @Override
    public List<Invitation> getInvitationsByEntreprise(Long entrepriseId) {
        return invitationRepository.findByEntrepriseId(entrepriseId);
    }

    @Override
    public Map<String, Long> getInvitationStatisticsByEntreprise(Long entrepriseId) {
        List<Invitation> invitations = invitationRepository.findByEntrepriseId(entrepriseId);

        long envoyees = invitations.stream()
                .filter(inv -> inv.getStatus() == InvitationStatus.ENVOYEE)
                .count();

        long acceptees = invitations.stream()
                .filter(inv -> inv.getStatus() == InvitationStatus.ACCEPTEE)
                .count();

        long expirees = invitations.stream()
                .filter(inv -> inv.getStatus() == InvitationStatus.EXPIREE)
                .count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("envoyees", envoyees);
        stats.put("acceptees", acceptees);
        stats.put("expirees", expirees);

        return stats;
    }



}
