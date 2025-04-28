package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
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
    @JsonIgnoreProperties("employes")  // ignore juste la liste des employés dans l’entreprise
    private Profession profession;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    @JsonIgnoreProperties("employes")  // ignore juste la liste des employés dans l’entreprise
    private Entreprise entreprise;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "numUser")
    @JsonIgnoreProperties({"entreprise", "employe","role"})
    private User user;
}