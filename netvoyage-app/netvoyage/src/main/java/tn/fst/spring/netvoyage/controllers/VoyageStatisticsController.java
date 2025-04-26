package tn.fst.spring.netvoyage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.fst.spring.netvoyage.repositories.VoyageRepository;
import java.util.*;
import java.time.format.TextStyle;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.Locale;

@RestController
@RequestMapping("/api/voyages/statistics")
public class VoyageStatisticsController {
    @Autowired
    private VoyageRepository voyageRepository;

    @GetMapping("")
    public Map<String, Object> getVoyageStatistics() {
        var voyages = voyageRepository.findAll();
        Map<String, Long> voyagesParDestination = voyages.stream()
                .collect(Collectors.groupingBy(v -> v.getDestination(), Collectors.counting()));
        Map<String, Long> voyagesParMois = voyages.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getDateDepart().getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH),
                        Collectors.counting()
                ));
        Map<String, Object> stats = new HashMap<>();
        stats.put("parDestination", voyagesParDestination);
        stats.put("parMois", voyagesParMois);
        stats.put("total", voyages.size());
        return stats;
    }
}
