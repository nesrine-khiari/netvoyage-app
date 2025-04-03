package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.CommentaireDTO;
import tn.fst.spring.netvoyage.dtos.CommentaireResponseDTO;
import tn.fst.spring.netvoyage.entities.Commentaire;

import java.util.List;

public interface ICommentaireService {
    List<Commentaire> getAllCommentaires();
    CommentaireResponseDTO addComment(Long publicationId, CommentaireDTO commentDTO);
}
