package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table( name ="Voyage")
@Getter
public class Voyage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="numVoyage")
    private Long numVoyage; // Clé primaire
    private String Nom;

    @OneToOne
    @JoinColumn(name = "discussion_id", unique = true)
    private Discussion discussion;
    @ManyToMany
    @JoinTable(
            name = "Voyage_Voyageur",
            joinColumns = @JoinColumn(name = "numVoyage"),
            inverseJoinColumns = @JoinColumn(name = "numVoyageur")
    )
    private Set<Voyageur> voyageurs;
}
