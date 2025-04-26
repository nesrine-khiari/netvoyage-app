package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.repositories.UserRepository;
import tn.fst.spring.netvoyage.services.interfaces.ICustomUserDetailsService;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements ICustomUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
