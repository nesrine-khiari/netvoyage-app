package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;


import lombok.*;

@Entity
@Table(name = "Discussion")
@Getter
@Setter
public class Discussion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numDiscussion")
    private Long numDiscussion; // Clé primaire
    private String name;
    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages;

    @OneToOne(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Voyage voyage;
}
