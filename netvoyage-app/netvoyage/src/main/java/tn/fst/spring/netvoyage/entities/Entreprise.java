package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Entreprise{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nomEntreprise;
        private String pays;
        private String logoUrl;
        private String adresse;
        private String telephone;

        @ManyToOne
        @JsonManagedReference // Evite la récursivité infinie lors de la sérialisation JSON
        private DomaineActivite domaine; // ✅ Relation avec domaine

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<Employe> employes;

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        private List<Invitation> invitations;

        @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
        private List<Voyage> voyages;


        @OneToOne
        @JoinColumn(name = "user_id", referencedColumnName = "numUser") // FK vers User
        private User user;

}
