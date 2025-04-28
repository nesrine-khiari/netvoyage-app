package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.fst.spring.netvoyage.entities.Employe;
import tn.fst.spring.netvoyage.entities.Voyageur;
import java.util.Optional;

@Repository
public interface VoyageurRepository extends JpaRepository<Voyageur, Long> {
    Optional<Voyageur> findById(Long id); // Correct method with Optional
    Optional<Voyageur> findByEmploye(Employe employe);
    boolean existsByEmploye_Id(Long employeId);
    Voyageur findByEmploye_Id(Long employeId);
}