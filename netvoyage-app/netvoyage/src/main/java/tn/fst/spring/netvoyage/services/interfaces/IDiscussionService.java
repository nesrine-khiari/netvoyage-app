package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.entities.Discussion;
import tn.fst.spring.netvoyage.entities.Message;

import java.util.List;

public interface IDiscussionService {
    Discussion createDiscussion(String name);
    Discussion getDiscussionById(Long id);
    List<Message> getMessages(Long discussionId);
}
