package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "voyages")
@Getter
@Setter
public class Voyage extends TimeStamp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(name = "date_depart", nullable = false)
    private LocalDate dateDepart;

    @Column(name = "date_retour", nullable = false)
    private LocalDate dateRetour;

    @Column(name = "objet_voyage", nullable = false, length = 100)
    private String objetVoyage;

    @Column(length = 200)
    private String perimetre;

    // Organisateur du voyage
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisateur_id", nullable = false)
    private Entreprise organisateur;

    // Participants
    @ManyToMany
    @JoinTable(
            name = "voyage_participants",
            joinColumns = @JoinColumn(name = "voyage_id"),
            inverseJoinColumns = @JoinColumn(name = "voyageur_id")
    )
    private Set<Voyageur> participants = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;

    // Discussion liée au voyage
    @OneToOne(mappedBy = "voyage", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Discussion discussion;

    // Méthodes utilitaires
    public void addParticipant(Voyageur voyageur) {
        this.participants.add(voyageur);
        voyageur.getVoyages().add(this);
    }

    public void removeParticipant(Voyageur voyageur) {
        this.participants.remove(voyageur);
        voyageur.getVoyages().remove(this);
    }

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (dateRetour.isBefore(dateDepart)) {
            throw new IllegalArgumentException("La date de retour doit être après la date de départ");
        }
    }
}