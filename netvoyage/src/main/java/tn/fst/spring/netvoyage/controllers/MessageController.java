package tn.fst.spring.netvoyage.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.entities.MotInterdit;
import tn.fst.spring.netvoyage.services.interfaces.IMessageService;
import tn.fst.spring.netvoyage.services.interfaces.IMotInterditService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @GetMapping("/discussion/{discussionId}")
    public List<Message>  getAllMessagesByDiscussion(@PathVariable Long discussionId) {
        return messageService.getMessagesByDiscussion(discussionId);
    }

}