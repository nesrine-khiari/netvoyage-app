package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "voyageurs")
@Getter
@Setter
public class Voyageur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employe_id", nullable = false, unique = true)
    private Employe employe;
    @ManyToMany(mappedBy = "seenBy") // In Message, 'seenBy' is the mapped collection
    private Set<Message> messagesSeen = new HashSet<>();

    @OneToMany(mappedBy = "sender") // Here we specify that the sender in Message is a Voyageur
    private Set<Message> sentMessages = new HashSet<>();

    @ManyToMany(mappedBy = "participants")
    private Set<Voyage> voyages = new HashSet<>();

    @OneToMany(mappedBy = "organisateur")
    private Set<Voyage> voyagesOrganises = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "discussion_voyageur",
            joinColumns = @JoinColumn(name = "voyageur_id"),
            inverseJoinColumns = @JoinColumn(name = "discussion_id")
    )
    private Set<Discussion> discussions = new HashSet<>();





}


