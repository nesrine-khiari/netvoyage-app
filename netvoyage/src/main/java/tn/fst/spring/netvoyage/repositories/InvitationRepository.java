package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.spring.netvoyage.entities.Invitation;
import tn.fst.spring.netvoyage.enums.InvitationStatus;
import java.time.LocalDateTime;


import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByToken(String token);
    List<Invitation> findByEntrepriseId(Long entrepriseId);
    List<Invitation> findByEntrepriseIdAndStatus(Long entrepriseId, InvitationStatus status);
    List<Invitation> findByStatusAndDateEnvoiBefore(InvitationStatus status, LocalDateTime date);
}

