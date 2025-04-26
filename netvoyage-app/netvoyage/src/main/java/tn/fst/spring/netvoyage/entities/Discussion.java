package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;


import lombok.*;

@Entity
@Table(name = "Discussion")
@Getter
@Setter
public class Discussion extends TimeStamp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numDiscussion")
    private Long numDiscussion; // Clé primaire
    private String name;
    @OneToMany(mappedBy = "discussion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Message> messages;

    @OneToOne
    @JoinColumn(name = "voyage_id", unique = true) // Ensuring a unique discussion per voyage
    private Voyage voyage;
}
