package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Commentaire;
import tn.fst.spring.netvoyage.repositories.CommentaireRepository;
import tn.fst.spring.netvoyage.services.interfaces.ICommentaireService;

import java.util.List;

@Service
public class CommentaireServiceImpl implements ICommentaireService {
    private final CommentaireRepository commentaireRepository;
    public CommentaireServiceImpl(CommentaireRepository commentaireRepository) {
        this.commentaireRepository = commentaireRepository;
    }
    public List<Commentaire> getAllCommentaires() {
        return commentaireRepository.findAll();
    }
}
