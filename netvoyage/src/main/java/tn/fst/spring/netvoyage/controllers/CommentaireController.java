package tn.fst.spring.netvoyage.controllers;


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


    @GetMapping
    public List<Commentaire> getAllCommentaires() {
        return commentaireService.getAllCommentaires();
    }
}
