package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Discussion;
import tn.fst.spring.netvoyage.entities.Message;
import tn.fst.spring.netvoyage.repositories.DiscussionRepository;
import tn.fst.spring.netvoyage.services.interfaces.IDiscussionService;
import tn.fst.spring.netvoyage.services.interfaces.IMessageService;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.repositories.UserRepository;


@Service
public class EntrepriseService {

    @Autowired
    private UserRepository userRepository;
    // Méthode pour récupérer l'ID de l'entreprise à partir du username
    public Long getEntrepriseIdByEmail(String email) {
        // Requête pour récupérer l'ID de l'entreprise à partir du username
        // Cela peut dépendre de la façon dont vous avez configuré votre modèle d'utilisateur et entreprise
        // Par exemple, vous pouvez avoir un User entity avec un lien vers l'entité Entreprise
        User user = userRepository.findByEmail(email).get();  // Cela récupère directement l'utilisateur
        System.out.println("Utilisateur trouvé : " + user);
        if (user != null && user.getEntreprise() != null) {
            System.out.println("Entreprise associée : " + user.getEntreprise().getId());
        } else {
            System.out.println("Utilisateur ou entreprise non trouvé");
        }

        // Exemple d'un utilisateur avec username
        return user.getEntreprise().getId(); // Récupérer l'ID de l'entreprise à partir de l'entité User
    }
}