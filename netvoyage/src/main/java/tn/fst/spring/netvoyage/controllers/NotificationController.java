package tn.fst.spring.netvoyage.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.dtos.MessageDTO;
import tn.fst.spring.netvoyage.dtos.NotificationPublicationDTO;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.services.interfaces.ICommentaireService;
import tn.fst.spring.netvoyage.services.interfaces.IPublicationService;
import tn.fst.spring.netvoyage.services.interfaces.IUserService;

import java.util.Optional;

@Controller
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ICommentaireService commentaireService;

    @Autowired
    private IPublicationService publicationService;

    @Autowired
    private IUserService userService;

    @MessageMapping("/publication/{publicationId}/comment")
    public void notifyPublicationOwner(MessageDTO messageDTO, @DestinationVariable Long publicationId) {
        Optional<Publication> publicationOpt = publicationService.findById(publicationId);

        if (publicationOpt.isPresent()) {
            Publication publication = publicationOpt.get();
            User owner = publication.getOwner(); // Ensure your entity has an owner field

            NotificationPublicationDTO notification = new NotificationPublicationDTO(
                    "New Comment",
                    "Your publication '" + publication.getTitle() + "' received a new comment!",
                    publicationId
            );

            // Send notification to the publication owner
            messagingTemplate.convertAndSend("/topic/notifications/" + owner.getNumUser(), notification);
        }
    }
}

