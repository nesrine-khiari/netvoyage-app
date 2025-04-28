package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.spring.netvoyage.entities.Reclamation;
import tn.fst.spring.netvoyage.entities.ReclamationStatus;
import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    // Trouve les réclamations par entreprise (via l'auteur)
    List<Reclamation> findByAuthor_Employe_Entreprise_Id(Long entrepriseId);

    // Trouve les réclamations par statut
    List<Reclamation> findByStatus(ReclamationStatus status);

    List<Reclamation> findByStatusAndAuthor_Employe_Entreprise_Id(ReclamationStatus status, Long entrepriseId);

    long countByStatus(ReclamationStatus status);

    long countByAuthor_Employe_Entreprise_Id(Long entrepriseId);

    long countByStatusAndAuthor_Employe_Entreprise_Id(ReclamationStatus status, Long entrepriseId);
}