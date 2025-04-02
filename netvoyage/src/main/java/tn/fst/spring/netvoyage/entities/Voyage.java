package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table( name ="Voyage")
@Getter
@Setter
public class Voyage extends TimeStamp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="numVoyage")
    private Long numVoyage; // Clé primaire
    private String Nom;

    @OneToOne(mappedBy = "voyage", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Discussion discussion;
    @ManyToMany
    @JoinTable(
            name = "Voyage_Voyageur",
            joinColumns = @JoinColumn(name = "numVoyage"),
            inverseJoinColumns = @JoinColumn(name = "numVoyageur")
    )
    private Set<Voyageur> voyageurs;
}
