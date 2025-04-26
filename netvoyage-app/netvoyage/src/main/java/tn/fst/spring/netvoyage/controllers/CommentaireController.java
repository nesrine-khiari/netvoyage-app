package tn.fst.spring.netvoyage.controllers;


import org.springframework.http.ResponseEntity;
import tn.fst.spring.netvoyage.dtos.CommentaireDTO;
import tn.fst.spring.netvoyage.dtos.PublicationDTO;
import tn.fst.spring.netvoyage.entities.Commentaire;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.services.interfaces.ICommentaireService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/commentaires")
public class CommentaireController {
    private final ICommentaireService commentaireService;
    public CommentaireController(ICommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }
    @PutMapping("/{commentId}/like/{userId}")
    public ResponseEntity<String> likeComment(@PathVariable Long commentId, @PathVariable Long userId) {
        commentaireService.likeComment(commentId, userId);
        return ResponseEntity.ok("Comment liked successfully");
    }

    @PutMapping("/{commentId}/dislike/{userId}")
    public ResponseEntity<String> dislikeComment(@PathVariable Long commentId, @PathVariable Long userId) {
        commentaireService.dislikeComment(commentId, userId);
        return ResponseEntity.ok("Comment disliked successfully");
    }

    @GetMapping
    public List<Commentaire> getAllCommentaires() {
        return commentaireService.getAllCommentaires();
    }
}
