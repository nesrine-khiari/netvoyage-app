package tn.fst.spring.netvoyage.controllers;


import org.springframework.http.ResponseEntity;
import tn.fst.spring.netvoyage.dtos.CommentaireDTO;
import tn.fst.spring.netvoyage.dtos.PublicationDTO;
import tn.fst.spring.netvoyage.entities.Commentaire;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.services.interfaces.ICommentaireService;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.services.interfaces.ISentimentService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commentaires")
public class CommentaireController {
    private final ICommentaireService commentaireService;

    private final ISentimentService sentimentService;

    public CommentaireController(ICommentaireService commentaireService, ISentimentService sentimentService) {
        this.commentaireService = commentaireService;
        this.sentimentService = sentimentService;
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

    // 🎯 1. Analyser tous les commentaires
    @GetMapping("/sentiments")
    public ResponseEntity<Map<Long, String>> analyzeAllCommentaires() {
        List<Commentaire> commentaires = commentaireService.getAllCommentaires();

        Map<Long, String> sentiments = commentaires.stream()
                .collect(Collectors.toMap(
                        Commentaire::getNumCommentaire,
                        c -> sentimentService.analyzeSentiment(c.getContent())
                ));

        return ResponseEntity.ok(sentiments);
    }

    // 🎯 2. Analyser un seul commentaire par son ID
    @GetMapping("/{commentId}/sentiment")
    public ResponseEntity<String> analyzeCommentaire(@PathVariable Long commentId) {
        Commentaire commentaire = commentaireService.getComment(commentId); // On suppose que tu as cette méthode dans ton service
        if (commentaire == null) {
            return ResponseEntity.notFound().build();
        }
        String sentiment = sentimentService.analyzeSentiment(commentaire.getContent());
        return ResponseEntity.ok(sentiment);
    }
}
