package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numPublication;
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "num_user", nullable = false)
    private User owner;


    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL)
    private Set<Commentaire> commentaires;

    @ManyToMany
    @JoinTable(
            name = "publication_likes",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy;

    @ManyToMany
    @JoinTable(
            name = "publication_dislikes",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> dislikedBy;
}