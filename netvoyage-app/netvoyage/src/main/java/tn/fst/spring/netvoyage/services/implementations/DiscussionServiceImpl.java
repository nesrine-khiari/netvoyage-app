package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Discussion;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.repositories.DiscussionRepository;
import tn.fst.spring.netvoyage.services.interfaces.IDiscussionService;
import tn.fst.spring.netvoyage.services.interfaces.IMessageService;

import java.util.List;
import java.util.Optional;

@Service
public class DiscussionServiceImpl implements IDiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private IMessageService messageService;

    @Override
    public Discussion createDiscussion(String name) {
        Discussion discussion = new Discussion();
        discussion.setName(name);
        return discussionRepository.save(discussion);
    }

    @Override
    public Discussion getDiscussionById(Long id) {
        return discussionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Message> getMessages(Long discussionId) {
        return messageService.getMessagesByDiscussion(discussionId);
    }
}
