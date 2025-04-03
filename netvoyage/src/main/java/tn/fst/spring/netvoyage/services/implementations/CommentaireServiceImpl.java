package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.dtos.CommentaireDTO;
import tn.fst.spring.netvoyage.dtos.CommentaireResponseDTO;
import tn.fst.spring.netvoyage.dtos.NotificationPublicationDTO;
import tn.fst.spring.netvoyage.entities.Commentaire;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.repositories.CommentaireRepository;
import tn.fst.spring.netvoyage.services.interfaces.ICommentaireService;
import tn.fst.spring.netvoyage.services.interfaces.IPublicationService;

import java.util.List;
import java.util.Optional;

@Service
public class CommentaireServiceImpl implements ICommentaireService {

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private IPublicationService publicationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public CommentaireResponseDTO addComment(Long publicationId, CommentaireDTO commentDTO) {
        Optional<Publication> publicationOpt = publicationService.findById(publicationId);

        if (publicationOpt.isPresent()) {
            Commentaire commentaire = new Commentaire();
            Publication publication = publicationOpt.get();
            commentaire.setPublication(publication);
            commentaire.setContent(commentDTO.getContent());
            Commentaire savedComment = commentaireRepository.save(commentaire); // Save the comment

            // Send notification to the owner of the publication
            User owner = publication.getOwner();
            NotificationPublicationDTO notification = new NotificationPublicationDTO(
                    "New Comment",
                    "Your publication '" + publication.getTitle() + "' received a new comment!",
                    publicationId
            );

            messagingTemplate.convertAndSend("/topic/notifications/" + owner.getNumUser(), notification);

            CommentaireResponseDTO responseDTO = new CommentaireResponseDTO();
            responseDTO.setContent(savedComment.getContent());
            responseDTO.setNumCommentaire(savedComment.getNumCommentaire());
            responseDTO.setNumOwner(owner.getNumUser());
            responseDTO.setNumPublication(publication.getNumPublication());
            return responseDTO;
        }
        throw new RuntimeException("Publication not found");
    }

    @Override
    public List<Commentaire> getAllCommentaires() {
        return List.of();
    }
}

