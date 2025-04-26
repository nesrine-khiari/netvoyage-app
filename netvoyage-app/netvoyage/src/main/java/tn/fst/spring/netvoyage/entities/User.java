package tn.fst.spring.netvoyage.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tn.fst.spring.netvoyage.enums.Role;

import java.time.Instant;
import java.util.List;
import java.util.Set;
@Entity
@Getter
@Setter
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numUser;
    private String email;
    private String password;
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Commentaire> commentaires;


    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Publication> publications;


    // ✅ Relation bidirectionnelle vers Entreprise
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "entreprise_id", referencedColumnName = "id")
    private Entreprise entreprise;

    // ✅ Relation bidirectionnelle vers Employe (même principe)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employe_id", referencedColumnName = "id")
    private Employe employe;
}