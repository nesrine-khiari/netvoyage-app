package tn.fst.spring.netvoyage.entities;


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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numUser;
    private String email;
    private String password;
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Commentaire> commentaires;


    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Publication> publications;
}
