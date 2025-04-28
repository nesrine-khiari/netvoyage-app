package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Entreprise {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id") // Assurez-vous que ce nom correspond exactement à votre colonne
        private Long id;

        @Column(name = "nom_entreprise") // Doit matcher exactement le nom de colonne
        private String nomEntreprise;

        @Column(name = "secteur") // Doit matcher exactement le nom de colonne
        private String secteur;

        @Column(name = "pays") // Doit matcher exactement le nom de colonne
        private String pays;

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        private List<User> employes;

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        private List<Invitation> invitations;


}
