package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.fst.spring.netvoyage.entities.Voyage;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    List<Voyage> findAllByEntreprise_Id(Long companyId);
    List<Voyage> findByEntrepriseId(Long entrepriseId);

    /**
     * Recherche des voyages correspondant aux critères de matching
     * @param dateDepart Date de départ approximative
     * @param dateRetour Date de retour approximative
     * @param destination Destination du voyage (peut être null)
     * @param perimetre Périmètre du voyage (peut être null)
     * @return Liste des voyages correspondant aux critères
     */
    @Query("SELECT v FROM Voyage v WHERE " +
            "v.dateDepart BETWEEN :dateDepart AND :dateRetour AND " +
            "(:destination IS NULL OR LOWER(v.destination) LIKE LOWER(CONCAT('%', :destination, '%'))) AND " +
            "(:perimetre IS NULL OR LOWER(v.perimetre) LIKE LOWER(CONCAT('%', :perimetre, '%')))")
    List<Voyage> findMatchingVoyages(
            @Param("dateDepart") LocalDate dateDepart,
            @Param("dateRetour") LocalDate dateRetour,
            @Param("destination") String destination,
            @Param("perimetre") String perimetre
    );

}