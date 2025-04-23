package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.spring.netvoyage.entities.Reclamation;
import tn.fst.spring.netvoyage.entities.ReclamationStatus;
import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    // Trouve les réclamations par entreprise (via l'auteur)
    List<Reclamation> findByAuthor_Entreprise_NumEntreprise(Long numEntreprise);

    // Trouve les réclamations par statut
    List<Reclamation> findByStatus(ReclamationStatus status);

    List<Reclamation> findByStatusAndAuthor_Entreprise_NumEntreprise(ReclamationStatus status, Long numEntreprise);
}