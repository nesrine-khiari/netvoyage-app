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
import tn.fst.spring.netvoyage.services.interfaces.IUserService;

import java.util.List;
import java.util.Optional;

@Service
public class CommentaireServiceImpl implements ICommentaireService {

    @Autowired
    private IUserService userService;

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private IPublicationService publicationService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public CommentaireResponseDTO addComment(Long publicationId, CommentaireDTO commentDTO) {
        Optional<Publication> publicationOpt = publicationService.findById(publicationId);
        User commentOwner = userService.getUserById(commentDTO.getNumOwner());
        if (publicationOpt.isPresent()) {
            Commentaire commentaire = new Commentaire();
            Publication publication = publicationOpt.get();
            commentaire.setPublication(publication);
            commentaire.setContent(commentDTO.getContent());
            commentaire.setOwner(commentOwner);
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

    @Override
    public void likeComment(Long commentId, Long userId) {
        Commentaire commentaire = commentaireRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userService.getUserById(userId);

        // Remove dislike if it exists
        commentaire.getDislikedBy().remove(user);

        // Check if user already liked it
        if (commentaire.getLikedBy().contains(user)) {
            commentaire.getLikedBy().remove(user);
        } else {
            commentaire.getLikedBy().add(user);
        }

        // Send notification to the owner of the comment
        NotificationPublicationDTO notification = new NotificationPublicationDTO(
                "New Reaction",
                "Your comment received a new like!",
                commentaire.getPublication().getNumPublication()
        );

        messagingTemplate.convertAndSend("/topic/notifications/" + commentaire.getOwner().getNumUser(), notification);

        commentaireRepository.save(commentaire);
    }

    @Override
    public void dislikeComment(Long commentId, Long userId) {
        Commentaire commentaire = commentaireRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userService.getUserById(userId);

        // Remove like if it exists
        commentaire.getLikedBy().remove(user);

        // Check if user already disliked it
        if (commentaire.getDislikedBy().contains(user)) {
            commentaire.getDislikedBy().remove(user);
        } else {
            commentaire.getDislikedBy().add(user);
        }

        // Send notification to the owner of the comment
        NotificationPublicationDTO notification = new NotificationPublicationDTO(
                "New Reaction",
                "Your comment received a new dislike!",
                commentaire.getPublication().getNumPublication()
        );

        messagingTemplate.convertAndSend("/topic/notifications/" + commentaire.getOwner().getNumUser(), notification);

        commentaireRepository.save(commentaire);
    }

}

