package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.spring.netvoyage.entities.Profession;

public interface ProfessionRepository extends JpaRepository<Profession, Long> {
}