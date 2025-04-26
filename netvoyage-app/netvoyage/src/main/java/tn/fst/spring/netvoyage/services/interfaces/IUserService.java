package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();
    User getUserById(Long id);
}

