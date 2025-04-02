package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.fst.spring.netvoyage.entities.Discussion;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
}
