package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.repositories.EntrepriseRepository;
import tn.fst.spring.netvoyage.repositories.UserRepository;
import tn.fst.spring.netvoyage.services.interfaces.IAuthService;
import tn.fst.spring.netvoyage.dtos.AuthRequest;
import tn.fst.spring.netvoyage.dtos.RegisterEntrepriseRequest;
import tn.fst.spring.netvoyage.dtos.AuthResponse;
import tn.fst.spring.netvoyage.entities.Entreprise;
import tn.fst.spring.netvoyage.enums.Role;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.utils.JWTUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    @Override
    public AuthResponse registerEntreprise(RegisterEntrepriseRequest req) {
        // Créer l'utilisateur
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setUsername(req.getUsername());
        user.setRole(Role.ENTREPRISE);

        // Créer l'entreprise
        Entreprise entreprise = new Entreprise();
        entreprise.setNomEntreprise(req.getNomEntreprise());
        entreprise.setPays(req.getPays());

        // Lier les deux
        user.setEntreprise(entreprise);
        entreprise.setUser(user);

        // Sauvegarder
        userRepository.save(user);
        entrepriseRepository.save(entreprise);

        // Générer le token
        String token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse("Entreprise registered successfully", token);
    }

    @Override
    public AuthResponse authenticate(AuthRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse("Authentication successful", token);
    }
}
