package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.awt.*;
import java.io.Serializable;
import java.util.Set;

import lombok.*;
@Entity
@Table( name ="Voyageur")
@Getter
public class Voyageur implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="numVoyageur")
    private Long numVoyageur; // Clé primaire
    private String firstName;
    private String lastname;
    private String email;
    private String phone;
    @ManyToMany
    @JoinTable(
            name = "Voyageur_Discussion",
            joinColumns = @JoinColumn(name = "numVoyageur"),
            inverseJoinColumns = @JoinColumn(name = "numDiscussion")
    )
    private Set<Discussion> discussions;

    @OneToMany(mappedBy = "voyageur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> sentMessages;
}
