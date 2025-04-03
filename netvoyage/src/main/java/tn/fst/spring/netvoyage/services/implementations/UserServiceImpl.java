package tn.fst.spring.netvoyage.services.implementations;


import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.repositories.UserRepository;
import tn.fst.spring.netvoyage.services.interfaces.IUserService;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

