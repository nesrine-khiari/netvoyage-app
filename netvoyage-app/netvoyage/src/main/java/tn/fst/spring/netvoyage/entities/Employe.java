package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "employes")
@Getter
@Setter
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String adresse;
    private String telephone;

    @ManyToOne
    @JoinColumn(name = "profession_id")
    @JsonBackReference
    private Profession profession;

    @ManyToOne
    private Entreprise entreprise;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "numUser")
    private User user;
}