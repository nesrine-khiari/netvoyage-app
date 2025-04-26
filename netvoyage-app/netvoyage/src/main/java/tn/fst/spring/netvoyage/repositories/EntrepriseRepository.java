package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.spring.netvoyage.entities.Entreprise;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
}
