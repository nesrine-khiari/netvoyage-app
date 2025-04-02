package tn.fst.spring.netvoyage.services.interfaces;


import tn.fst.spring.netvoyage.dtos.MessageDTO;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.entities.Voyageur;
import tn.fst.spring.netvoyage.entities.Discussion;

import java.util.List;

public interface IMessageService {
    MessageDTO saveMessage(String content, Long voyageurId, Long discussionId);
    List<Message> getMessagesByDiscussion(Long discussionId);
}
