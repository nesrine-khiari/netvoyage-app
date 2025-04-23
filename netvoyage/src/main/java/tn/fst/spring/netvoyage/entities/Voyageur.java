package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.awt.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Set;

import lombok.*;
import tn.fst.spring.netvoyage.entities.Entreprise;

@Entity
@Table( name ="Voyageur")
@Getter
@Setter
public class Voyageur extends TimeStamp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="numVoyageur")
    private Long numVoyageur; // Clé primaire
    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Message> sentMessages;

    @ManyToMany(mappedBy = "voyageurs")
    @JsonIgnore
    private Set<Voyage> voyages;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise; // Obligatoire pour le filtrage

    public Long getNumVoyageur() {
        return this.numVoyageur;
    }

}
