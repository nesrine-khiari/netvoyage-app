package tn.fst.spring.netvoyage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.fst.spring.netvoyage.dtos.ChatStatisticsDTO;
import tn.fst.spring.netvoyage.dtos.StatisticsDTO;
import tn.fst.spring.netvoyage.services.interfaces.IStatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private IStatisticsService statisticsService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<StatisticsDTO> getUserStatistics(@PathVariable Long userId) {


        StatisticsDTO stats = statisticsService.getUserStatistics(userId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/user/{userId}/chat")
    public ResponseEntity<ChatStatisticsDTO> getUserChatStatistics(@PathVariable Long userId) {
        ChatStatisticsDTO chatStats = statisticsService.getChatStatistics(userId);
        return ResponseEntity.ok(chatStats);
    }

}
