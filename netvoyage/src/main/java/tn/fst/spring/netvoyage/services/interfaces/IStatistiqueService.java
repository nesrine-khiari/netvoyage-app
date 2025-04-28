package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

public interface IStatistiqueService {

    List<Object[]> getUsersByMonth();

    List<Object[]> getUsersByRole();

    List<User> getUsersByDateRangeAndRole(LocalDateTime start, LocalDateTime end, Role role);

}
