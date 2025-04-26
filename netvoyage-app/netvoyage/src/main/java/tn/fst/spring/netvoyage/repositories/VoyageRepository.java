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
    // List<Voyage> findByEmployeId(Long employeId);

    // 1. Requête de matching (destination + objet + date)
    /*List<Voyage> findByDestinationAndObjetVoyage_CodeAndDateDepartBetween(
            String destination,
            String objetCode,
            LocalDate dateStart,
            LocalDate dateEnd
    );

    // 2. Recherche par organisateur
    List<Voyage> findByOrganisateur_Id(Long organisateurId);

    // 3. Recherche flexible par destination
    List<Voyage> findByDestinationContainingIgnoreCase(String keyword);
    List<Voyage> findByEmployeId(Long employeId);



    // 4. Matching avancé SANS domaine d’activité
    @Query("SELECT v FROM Voyage v WHERE " +
            "v.destination = :destination AND " +
            "v.dateDepart BETWEEN :startDate AND :endDate AND " +
            "v.objetVoyage.code = :objetCode")
    List<Voyage> findMatchingVoyages(
            @Param("destination") String destination,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("objetCode") String objetCode
    );

    // 5. Voyages où un participant est inscrit
    @Query("SELECT v FROM Voyage v JOIN v.participants p WHERE p.id = :employeId")
    List<Voyage> findVoyagesByParticipantId(@Param("employeId") Long employeId);

    List<Voyage> findByDestinationAndDateDepartBetween(String destination, LocalDate start, LocalDate end);*/
}
