package tn.fst.spring.netvoyage.repositories;

import tn.fst.spring.netvoyage.entities.DomaineActivite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DomaineActiviteRepository extends JpaRepository<DomaineActivite, Long> {
    Optional<DomaineActivite> findByNomIgnoreCase(String nom);

}