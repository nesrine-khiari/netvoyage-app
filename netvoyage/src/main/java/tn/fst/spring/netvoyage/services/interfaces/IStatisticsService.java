package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.StatisticsDTO;

public interface IStatisticsService {
    StatisticsDTO getUserStatistics(Long userId);
}
