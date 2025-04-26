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
}

