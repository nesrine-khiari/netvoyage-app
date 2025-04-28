package tn.fst.spring.netvoyage.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.enums.Role;
import tn.fst.spring.netvoyage.services.interfaces.IStatistiqueService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user/statistiques")
@RequiredArgsConstructor
public class StatistiqueController {
    private final IStatistiqueService statistiqueService;

    @GetMapping("/users-by-month")
    public List<Object[]> getUsersByMonth() {
        return statistiqueService.getUsersByMonth();
    }

    @GetMapping("/users-by-role")
    public List<Object[]> getUsersByRole() {
        return statistiqueService.getUsersByRole();
    }

    @GetMapping("/users-by-date-and-role")
    public List<User> getUsersByDateAndRole(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam Role role) {
        return statistiqueService.getUsersByDateRangeAndRole(startDate, endDate, role);
    }

}
