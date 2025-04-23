package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "reclamations")
public class Reclamation extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numReclamation")
    private Long numReclamation;
    private String titre;
    private String description;

    @Enumerated(EnumType.STRING)
    private ReclamationStatus status = ReclamationStatus.IN_PROGRESS; // Statut par défaut

    // Relations
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Voyageur author;

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private Voyageur target;

    @ManyToOne
    @JoinColumn(name = "voyage_id")
    private Voyage voyage;

    // Ajoutez explicitement ce getter
    public Long getNumReclamation() {
        return this.numReclamation;
    }
}