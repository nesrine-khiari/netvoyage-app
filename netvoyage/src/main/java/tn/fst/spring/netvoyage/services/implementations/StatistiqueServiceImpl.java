package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.enums.Role;
import tn.fst.spring.netvoyage.repositories.UserRepository;
import tn.fst.spring.netvoyage.services.interfaces.IStatistiqueService;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatistiqueServiceImpl implements IStatistiqueService {


    private final UserRepository userRepository;

    @Override
    public List<Object[]> getUsersByMonth() {
        return userRepository.countUsersByMonth();
    }

    @Override
    public List<Object[]> getUsersByRole() {
        return userRepository.countUsersByRole();
    }

    @Override
    public List<User> getUsersByDateRangeAndRole(LocalDateTime start, LocalDateTime end, Role role) {
        return userRepository.findUsersByDateRangeAndRole(start, end, role);
    }
}
