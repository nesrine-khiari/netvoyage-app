package tn.fst.spring.netvoyage.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.spring.netvoyage.entities.Employe;

public interface EmployeRepository extends JpaRepository<Employe, Long> {
}