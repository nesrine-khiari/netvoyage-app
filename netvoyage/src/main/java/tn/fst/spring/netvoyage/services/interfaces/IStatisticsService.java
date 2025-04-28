package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.ChatStatisticsDTO;
import tn.fst.spring.netvoyage.dtos.StatisticsDTO;

public interface IStatisticsService {
    StatisticsDTO getUserStatistics(Long userId);
    ChatStatisticsDTO getChatStatistics(Long userId);

}
