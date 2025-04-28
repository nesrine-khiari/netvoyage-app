package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.spring.netvoyage.entities.Voyage;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {}