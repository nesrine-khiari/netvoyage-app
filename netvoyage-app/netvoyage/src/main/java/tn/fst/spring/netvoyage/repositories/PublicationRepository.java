package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.fst.spring.netvoyage.entities.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    @Query("SELECT COUNT(u) FROM Publication p JOIN p.likedBy u WHERE p.numPublication = :publicationId")
    long countLikes(@Param("publicationId") Long publicationId);

    @Query("SELECT COUNT(u) FROM Publication p JOIN p.dislikedBy u WHERE p.numPublication = :publicationId")
    long countDislikes(@Param("publicationId") Long publicationId);

}
