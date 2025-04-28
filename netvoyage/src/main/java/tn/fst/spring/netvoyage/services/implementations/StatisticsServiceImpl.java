package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.dtos.ChatStatisticsDTO;
import tn.fst.spring.netvoyage.dtos.StatisticsDTO;
import tn.fst.spring.netvoyage.entities.Publication;
import tn.fst.spring.netvoyage.repositories.CommentaireRepository;
import tn.fst.spring.netvoyage.repositories.EmployeRepository;
import tn.fst.spring.netvoyage.repositories.MessageRepository;
import tn.fst.spring.netvoyage.repositories.PublicationRepository;
import tn.fst.spring.netvoyage.services.interfaces.IStatisticsService;

import java.util.List;

@Service
public class StatisticsServiceImpl implements IStatisticsService {
    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private CommentaireRepository commentRepository;

    @Autowired
    private MessageRepository messageRepository;



    @Override
    public StatisticsDTO getUserStatistics(Long userId) {
        long totalPublications = publicationRepository.countPublicationsByUser(userId);


        long totalComments = commentRepository.countCommentsByUser(userId);
        long totalLikes = publicationRepository.countLikesByUser(userId);
        long totalDislikes =  publicationRepository.countDislikes(userId);

        return new StatisticsDTO(totalPublications, totalComments, totalLikes, totalDislikes);
    }
    @Override
    public ChatStatisticsDTO getChatStatistics(Long userId) {
        long totalMessagesSent = messageRepository.countMessagesByVoyageur(userId);
        long totalMessagesWithForbiddenWords = messageRepository.countMessagesWithForbiddenWords(userId, "%***%");

        double forbiddenWordsPercentage = totalMessagesSent == 0 ? 0 : (totalMessagesWithForbiddenWords * 100.0) / totalMessagesSent;

        long messagesSentToday = messageRepository.countMessagesSentToday(userId);

        double averageWordsPerMessage = messageRepository.calculateAverageWordsPerMessage(userId);

        int longestMessageLength = messageRepository.findLongestMessageLength(userId);

        return new ChatStatisticsDTO(
                totalMessagesSent,
                totalMessagesWithForbiddenWords,
                forbiddenWordsPercentage,
                messagesSentToday,
                averageWordsPerMessage,
                longestMessageLength
        );
    }



}
