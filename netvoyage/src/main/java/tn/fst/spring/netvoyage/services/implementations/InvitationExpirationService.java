package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Invitation;
import tn.fst.spring.netvoyage.enums.InvitationStatus;
import tn.fst.spring.netvoyage.repositories.InvitationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationExpirationService {

    private final InvitationRepository invitationRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Tous les jours à minuit
    public void expireOldInvitations() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Invitation> expiredInvitations = invitationRepository.findByStatusAndDateEnvoiBefore(
                InvitationStatus.ENVOYEE, sevenDaysAgo);
        for (Invitation invitation : expiredInvitations) {
            invitation.setStatus(InvitationStatus.EXPIREE);
            invitationRepository.save(invitation);
        }
    }
}