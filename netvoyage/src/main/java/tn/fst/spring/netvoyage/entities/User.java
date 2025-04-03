package tn.fst.spring.netvoyage.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Commentaire> commentaires;


    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Publication> publications;
}
