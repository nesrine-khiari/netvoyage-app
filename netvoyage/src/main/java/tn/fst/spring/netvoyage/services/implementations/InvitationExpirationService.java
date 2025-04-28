package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Invitation;
import tn.fst.spring.netvoyage.enums.InvitationStatus;
import tn.fst.spring.netvoyage.repositories.InvitationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class InvitationExpirationService {

    private final InvitationRepository invitationRepository;

    // Vérification toutes les minutes (pour réactivité)
    @Scheduled(cron = "0 * * * * ?")
    public void expireOldInvitations() {
        Instant sevenDaysAgo = Instant.now().minus(7, ChronoUnit.DAYS);

        List<Invitation> expiredInvitations = invitationRepository
                .findByStatusAndDateEnvoiBefore(InvitationStatus.ENVOYEE, sevenDaysAgo);

        expiredInvitations.forEach(invitation -> {
            invitation.setStatus(InvitationStatus.EXPIREE);
            invitationRepository.save(invitation);
        });
    }
}