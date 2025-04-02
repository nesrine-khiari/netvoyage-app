package tn.fst.spring.netvoyage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tn.fst.spring.netvoyage.dtos.MessageDTO;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.entities.Discussion;
import tn.fst.spring.netvoyage.services.interfaces.IDiscussionService;
import tn.fst.spring.netvoyage.services.interfaces.IMessageService;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IDiscussionService discussionService;

    // Handle sending a message to a specific discussion
    @MessageMapping("/chat/{discussionId}")
    public void sendMessageToDiscussion(MessageDTO messageDTO, @DestinationVariable Long discussionId) {
        // Save message and return DTO
        MessageDTO savedMessage = messageService.saveMessage(
                messageDTO.getContent(),
                messageDTO.getSenderId(), // Include sender ID in DTO
                discussionId
        );

        // Send DTO to the WebSocket topic
        messagingTemplate.convertAndSend("/topic/discussion/" + discussionId, savedMessage);
    }
}

