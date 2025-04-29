package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.fst.spring.netvoyage.entities.Commentaire;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    @Query("SELECT COUNT(c) FROM Commentaire c WHERE c.owner.numUser = :userId")
    long countCommentsByUser(@Param("userId") Long userId);

}
