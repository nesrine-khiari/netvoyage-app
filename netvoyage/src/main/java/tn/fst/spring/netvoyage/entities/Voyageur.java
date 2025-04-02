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
    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "voyageur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> sentMessages;

    @ManyToMany(mappedBy = "voyageurs")
    private Set<Voyage> voyages;

}
