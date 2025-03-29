package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;


import lombok.*;

@Entity
@Table(name = "Discussion")
@Getter
public class Discussion extends TimeStamp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numDiscussion")
    private Long numDiscussion; // Clé primaire
    private String name;
    @ManyToMany(mappedBy = "discussions")
    private Set<Voyageur> voyageurs;
    @OneToMany
    @JoinTable(
            name= "discussion_messages",
            joinColumns = @JoinColumn(name = "numDiscussion"),
            inverseJoinColumns = @JoinColumn(name="numMessage")
    )
    private Set<Message> messages;
}
