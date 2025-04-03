package tn.fst.spring.netvoyage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.fst.spring.netvoyage.dtos.CommentaireDTO;
import tn.fst.spring.netvoyage.dtos.CommentaireResponseDTO;
import tn.fst.spring.netvoyage.dtos.PublicationDTO;
import tn.fst.spring.netvoyage.entities.Commentaire;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.services.interfaces.ICommentaireService;
import tn.fst.spring.netvoyage.services.interfaces.IPublicationService;

import java.util.List;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    @Autowired
    private  IPublicationService publicationService;

    @Autowired
    private  ICommentaireService commentaireService;



    @PostMapping
    public Publication addPublication(@RequestBody PublicationDTO publication) {
        return publicationService.addPublication(publication);
    }
    @PostMapping("/{publicationId}/comments")
    public ResponseEntity<CommentaireResponseDTO> addComment(
            @PathVariable Long publicationId,
            @RequestBody CommentaireDTO commentDTO) {
        CommentaireResponseDTO savedComment = commentaireService.addComment(publicationId, commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @PutMapping("/{publicationId}/like/{userId}")
    public ResponseEntity<String> likePublication(@PathVariable Long publicationId, @PathVariable Long userId) {
        publicationService.likePublication(publicationId, userId);
        return ResponseEntity.ok("Publication liked successfully");
    }

    @PutMapping("/{publicationId}/dislike/{userId}")
    public ResponseEntity<String> dislikePublication(@PathVariable Long publicationId, @PathVariable Long userId) {
        publicationService.dislikePublication(publicationId, userId);
        return ResponseEntity.ok("Publication disliked successfully");
    }


    @DeleteMapping("/{id}")
    public void deletePublication(@PathVariable Long id) {
         publicationService.deletePublication(id);
    }


    @GetMapping
    public List<Publication> getAllPublications() {
        return publicationService.findAll();
    }
}