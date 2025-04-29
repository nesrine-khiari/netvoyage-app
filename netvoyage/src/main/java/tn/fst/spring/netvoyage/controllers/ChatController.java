package tn.fst.spring.netvoyage.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.fst.spring.netvoyage.dtos.MessageDTO;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.entities.Discussion;
import tn.fst.spring.netvoyage.entities.Voyageur;
import tn.fst.spring.netvoyage.services.interfaces.IDiscussionService;
import tn.fst.spring.netvoyage.services.interfaces.IMessageService;
import tn.fst.spring.netvoyage.services.interfaces.IMotInterditService;
import tn.fst.spring.netvoyage.services.interfaces.IVoyageurService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private IDiscussionService discussionService;

    // Handle sending a message to a specific discussion
    @MessageMapping("/discussion/{discussionId}")
    public void sendMessageToDiscussion(MessageDTO messageDTO, @DestinationVariable Long discussionId) {
        System.out.println("📨 Received message: " + messageDTO.getContent() + " from senderId: " + messageDTO.getSenderId());
        Boolean isBanned = voyageurService.isBanned(messageDTO.getSenderId());
        if (isBanned) {
            messagingTemplate.convertAndSend("/topic/ban/" + messageDTO.getSenderId(), "🚫 You are banned you cannot send messages to this discussion.");
            return;
        }

        List<String> forbiddenWords = motInterditService.getAllMotInterdits();
        System.out.println("🚫 Forbidden words list: " + forbiddenWords);

        int forbiddenWordCount = 0;
        String content = messageDTO.getContent();

        for (String forbiddenWord : forbiddenWords) {
            // Use regex to match each occurrence of the forbidden word
            String regex = "\\b" + Pattern.quote(forbiddenWord.toLowerCase()) + "\\b";
            Matcher matcher = Pattern.compile(regex).matcher(content.toLowerCase());

            while (matcher.find()) {
                forbiddenWordCount++;
                System.out.println("⚠️ Forbidden word detected: " + forbiddenWord);
            }

            // Replace all occurrences with ***
            content = content.replaceAll("(?i)\\b" + Pattern.quote(forbiddenWord) + "\\b", "***");
        }

        System.out.println("🔎 Total forbidden words found: " + forbiddenWordCount);

        if (forbiddenWordCount >= 5) {
            System.out.println("🚨 Forbidden word limit exceeded. Banning user: " + messageDTO.getSenderId());
            voyageurService.banVoyageur(messageDTO.getSenderId());
            messagingTemplate.convertAndSend("/topic/ban/" + messageDTO.getSenderId(), "🚫 You have been banned for using forbidden words.");
            System.out.println("🚫 Ban notification sent. Message processing stopped.");
            return;
        }

        System.out.println("✅ Message is safe. Saving and broadcasting...");

        MessageDTO savedMessage = messageService.saveMessage(content, messageDTO.getSenderId(), discussionId);

        messagingTemplate.convertAndSend("/topic/discussion/" + discussionId, savedMessage);

        System.out.println("📤 Message broadcasted to /topic/discussion/" + discussionId);
    }


    // Handle marking a message as seen
    @MessageMapping("/message/read/{discussionId}")
    @Transactional
    public void markMessageAsRead(Long voyageurId, @DestinationVariable Long discussionId) {
        Optional<Voyageur> voyageurOpt = voyageurService.getVoyageurById(voyageurId);

        if (voyageurOpt.isPresent()) {
            List<Message> messages = discussionService.getMessages(discussionId);
            for (Message message : messages) {
                message.getSeenBy().add(voyageurOpt.get());
                messageService.updateMessage(message);
                System.out.println("📌 Message " + message.getNumMessage() + " marqué comme lu par voyageur " + voyageurOpt.get().getEmploye().getId());
            }

        }
        MessageDTO message = new MessageDTO("marqué comme lu", voyageurId, voyageurOpt.get().getEmploye().getFirstname());

        //TODO: send to discussion socket
        messagingTemplate.convertAndSend("/topic/discussion/" + discussionId, message);

    }

    // VoyageurController.java
    @GetMapping("/api/voyageurs/{id}/is-banned")
    public ResponseEntity<Map<String, Boolean>> isBanned(@PathVariable Long id) {
        boolean banned = voyageurService.isBanned(id);
        return ResponseEntity.ok(Collections.singletonMap("banned", banned));
    }

}

