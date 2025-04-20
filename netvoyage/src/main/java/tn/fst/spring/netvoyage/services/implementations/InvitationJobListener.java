package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class InvitationJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Début du traitement batch des invitations");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Fin du traitement batch des invitations");
        System.out.println("Statut: " + jobExecution.getStatus());
        System.out.println("Nombre d'éléments traités: " +
                jobExecution.getStepExecutions().stream()
                        .mapToLong(step -> step.getWriteCount())
                        .sum());
    }
}