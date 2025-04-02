package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends TimeStamp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numMessage")
    private Long numMessage; // Primary Key

    private String content;

    // Many-to-Many relation for seen messages
    @ManyToMany
    @JoinTable(
            name = "Message_Voyageur",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "voyageur_id")
    )
    private Set<Voyageur> seenBy;

    // Many-to-One relation with Voyageur (the sender)
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Voyageur sender;

    @ManyToOne
    @JoinColumn(name = "discussion_id", nullable = false) // Link message to a discussion
    private Discussion discussion;
}
