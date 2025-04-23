package tn.fst.spring.netvoyage.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.User;
import tn.fst.spring.netvoyage.repositories.ReclamationRepository;
import tn.fst.spring.netvoyage.entities.Reclamation;
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final ReclamationRepository reclamationRepo;

    public boolean isUserFromCompany(Long companyId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getEntreprise().getNumEntreprise().equals(companyId);
    }

    public boolean canManageReclamation(Long reclamationId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Reclamation reclamation = reclamationRepo.findById(reclamationId).orElseThrow();

        // Utilisez les getters corrects
        return user.getEntreprise().getNumEntreprise().equals(
                reclamation.getAuthor().getEntreprise().getNumEntreprise());
    }
}