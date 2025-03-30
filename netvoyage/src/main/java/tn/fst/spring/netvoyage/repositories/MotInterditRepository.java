package tn.fst.spring.netvoyage.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.fst.spring.netvoyage.entities.MotInterdit;

import java.util.Optional;

public interface MotInterditRepository extends JpaRepository<MotInterdit, Long> {
    void deleteByMotIgnoreCase(String word);
    Optional<MotInterdit> findByMotIgnoreCase(String word);



}