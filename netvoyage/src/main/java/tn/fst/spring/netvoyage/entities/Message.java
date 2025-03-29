package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

import lombok.*;

@Entity
@Table(name = "Message")
@Getter
public class Message extends TimeStamp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numMessage")
    private Long numMessage; // Clé primaire
    private String content;
    @ManyToMany
    @JoinTable(
            name = "Message_Voyageur",
            joinColumns = @JoinColumn(name = "numMessage"),
            inverseJoinColumns = @JoinColumn(name = "numVoyageur")
    )
    private Set<Voyageur> seenBy;

    @ManyToOne
    @JoinColumn(name = "numVoyageur", nullable = false) // Foreign key to Voyageur
    private Voyageur voyageur;
}
