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
        private Long id;

        private String nomEntreprise;
        private String secteur;
        private String pays;

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        private List<User> employes;

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        private List<Invitation> invitations;


}
