package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.*;
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
        private String pays;
        private String logoUrl;
        private String adresse;
        private String telephone;

        @ManyToOne
        @JsonBackReference
        private DomaineActivite domaine;

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        @JsonIgnoreProperties({"profession", "entreprise", "user"})  // Ignore les informations non nécessaires des employés
        private List<Employe> employes;

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        @JsonBackReference
        private List<Invitation> invitations;

        @OneToOne
        @JoinColumn(name = "user_id", referencedColumnName = "numUser")
        @JsonIgnoreProperties({"entreprise", "employe", "role"})  // Ignore les attributs spécifiques à l'utilisateur
        private User user;
}