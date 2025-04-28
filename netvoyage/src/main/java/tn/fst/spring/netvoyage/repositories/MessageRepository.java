package tn.fst.spring.netvoyage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.entities.Discussion;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByDiscussion(Discussion discussion); // Find messages by discussion
    @Query("SELECT m FROM Message m LEFT JOIN FETCH m.seenBy WHERE m.numMessage = :messageId")
    Optional<Message> findByIdWithSeenBy(@Param("messageId") Long messageId);
    // Compter tous les messages envoyés par un utilisateur
    @Query("SELECT COUNT(m) FROM Message m WHERE m.sender.id = :voyageurId")
    long countMessagesByVoyageur(@Param("voyageurId") Long voyageurId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.sender.id = :voyageurId AND m.content LIKE :pattern")
    long countMessagesWithForbiddenWords(@Param("voyageurId") Long voyageurId, @Param("pattern") String pattern);



    @Query("SELECT COUNT(m) FROM Message m WHERE m.sender.id = :userId AND DATE(m.createdAt) = CURRENT_DATE")
    long countMessagesSentToday(@Param("userId") Long userId);

    @Query("SELECT AVG(LENGTH(m.content) - LENGTH(REPLACE(m.content, ' ', '')) + 1) FROM Message m WHERE m.sender.id = :userId")
    double calculateAverageWordsPerMessage(@Param("userId") Long userId);

    @Query("SELECT MAX(LENGTH(m.content)) FROM Message m WHERE m.sender.id = :userId")
    int findLongestMessageLength(@Param("userId") Long userId);

}

