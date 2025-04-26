package tn.fst.spring.netvoyage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numCommentaire;
    private String content;

    @ManyToOne
    @JoinColumn(name = "num_user")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    private Publication publication;

    @ManyToMany
    @JoinTable(
            name = "commentaire_likes",
            joinColumns = @JoinColumn(name = "commentaire_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy;

    @ManyToMany
    @JoinTable(
            name = "commentaire_dislikes",
            joinColumns = @JoinColumn(name = "commentaire_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> dislikedBy;
}
