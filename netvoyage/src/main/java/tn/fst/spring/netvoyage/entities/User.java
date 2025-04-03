package tn.fst.spring.netvoyage.entities;


import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numUser;
    private String email;
    private String password;
    private String username;

}
