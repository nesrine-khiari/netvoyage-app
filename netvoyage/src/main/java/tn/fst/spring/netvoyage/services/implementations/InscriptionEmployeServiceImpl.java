package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Employe;
import tn.fst.spring.netvoyage.entities.Invitation;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.enums.InvitationStatus;
import tn.fst.spring.netvoyage.enums.Role;
import tn.fst.spring.netvoyage.repositories.EmployeRepository;
import tn.fst.spring.netvoyage.repositories.InvitationRepository;
import tn.fst.spring.netvoyage.repositories.UserRepository;
import tn.fst.spring.netvoyage.services.interfaces.InscriptionEmployeService;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class InscriptionEmployeServiceImpl implements InscriptionEmployeService {

    private final InvitationRepository invitationRepository;
    private final EmployeRepository employeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public String traiterInscription(String token, String password, String firstname, String lastname, String telephone) {

        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invitation introuvable"));

        if (invitation.getStatus() != InvitationStatus.ENVOYEE) {
            throw new RuntimeException("Invitation déjà utilisée ou expirée");
        }

        // Création de l'employé
        Employe employe = new Employe();
        employe.setFirstname(firstname);
        employe.setLastname(lastname);
        employe.setAdresse(invitation.getEmailInvite());
        employe.setTelephone(telephone);
        employe.setEntreprise(invitation.getEntreprise());
        employeRepository.save(employe);

        // Création du user
        User user = new User();
        user.setEmail(invitation.getEmailInvite());
        user.setPassword(passwordEncoder.encode(password));
        user.setEmploye(employe);
        user.setRole(Role.EMPLOYE);
        userRepository.save(user);

        employe.setUser(user);
        employeRepository.save(employe);

        // Mise à jour de l'invitation
        invitation.setStatus(InvitationStatus.ACCEPTEE);
        invitation.setDateAcceptation(Instant.now());
        invitationRepository.save(invitation);

        return "Inscription réussie";
    }
}