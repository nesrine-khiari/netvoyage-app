package tn.fst.spring.netvoyage.services.implementations;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.dtos.MessageDTO;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.entities.Voyageur;
import tn.fst.spring.netvoyage.entities.Discussion;
import tn.fst.spring.netvoyage.repositories.DiscussionRepository;
import tn.fst.spring.netvoyage.repositories.MessageRepository;
import tn.fst.spring.netvoyage.repositories.VoyageurRepository;
import tn.fst.spring.netvoyage.services.interfaces.IMessageService;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private VoyageurRepository voyageurRepository;
    @Override
    public MessageDTO saveMessage(String content, Long voyageurId, Long discussionId) {
        Voyageur voyageur = voyageurRepository.findById(voyageurId)
                .orElseThrow(() -> new RuntimeException("Voyageur not found"));
        Discussion discussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new RuntimeException("Discussion not found"));

        Message message = new Message();
        message.setContent(content);
        message.setSender(voyageur);
        message.setDiscussion(discussion);

        Message savedMessage = messageRepository.save(message);

        // Convert Entity → DTO
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setNumMessage(savedMessage.getNumMessage());
        messageDTO.setContent(savedMessage.getContent());
        messageDTO.setSenderName(savedMessage.getSender().getEmploye().getFirstname()); // Assuming Voyageur has `name`
        messageDTO.setDiscussionId(savedMessage.getDiscussion().getNumDiscussion());

        return messageDTO;
    }

    @Override
    public List<Message> getMessagesByDiscussion(Long discussionId) {
        Optional<Discussion> discussion = discussionRepository.findById(discussionId);
        return discussion.map(messageRepository::findByDiscussion).orElse(null);
    }
    @Override
    @Transactional
    public Optional<Message> findById(Long messageId) {
        return messageRepository.findByIdWithSeenBy(messageId);
    }


    @Override
    public void updateMessage(Message message) {
        messageRepository.save(message);
    }
}
