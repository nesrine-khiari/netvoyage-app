package tn.fst.spring.netvoyage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tn.fst.spring.netvoyage.dtos.MessageDTO;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.entities.Discussion;
import tn.fst.spring.netvoyage.entities.Voyageur;
import tn.fst.spring.netvoyage.services.interfaces.IDiscussionService;
import tn.fst.spring.netvoyage.services.interfaces.IMessageService;
import tn.fst.spring.netvoyage.services.interfaces.IMotInterditService;
import tn.fst.spring.netvoyage.services.interfaces.IVoyageurService;

import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IMotInterditService motInterditService;

    @Autowired
    private IVoyageurService voyageurService;

    // Handle sending a message to a specific discussion
    @MessageMapping("/discussion/{discussionId}")
    public void sendMessageToDiscussion(MessageDTO messageDTO, @DestinationVariable Long discussionId) {
        // Retrieve forbidden words
        List<String> forbiddenWords = motInterditService.getAllMotInterdits();

        // Check if message contains forbidden words
        for (String forbiddenWord : forbiddenWords) {
            messageDTO.setContent(messageDTO.getContent().replaceAll("\\b" + forbiddenWord + "\\b", "***"));
        }


        // Save message and return DTO
        MessageDTO savedMessage = messageService.saveMessage(
                messageDTO.getContent(),
                messageDTO.getSenderId(),
                discussionId
        );

        // Send DTO to WebSocket topic
        messagingTemplate.convertAndSend("/topic/discussion/" + discussionId, savedMessage);
    }
    // Handle marking a message as seen
    @MessageMapping("/message/read")
    public void markMessageAsRead(MessageDTO messageDTO) {
        Optional<Message> messageOpt = messageService.findById(messageDTO.getNumMessage());
        Optional<Voyageur> voyageurOpt = voyageurService.getVoyageurById(messageDTO.getSenderId());

        if (messageOpt.isPresent() && voyageurOpt.isPresent()) {
            Message message = messageOpt.get();
            message.getSeenBy().add(voyageurOpt.get());
            messageService.updateMessage(message);

            System.out.println("📌 Message " + message.getNumMessage() + " marqué comme lu par voyageur " + messageDTO.getSenderId());
        }

        //TODO: send to discussion socket
    }
}

