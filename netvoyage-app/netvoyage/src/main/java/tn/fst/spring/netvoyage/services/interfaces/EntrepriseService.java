package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.EntrepriseDTO;
import tn.fst.spring.netvoyage.entities.Entreprise;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EntrepriseService {
    Entreprise updateEntreprise(Long id, EntrepriseDTO dto, MultipartFile logo) throws IOException;
    List<Entreprise> getAllEntreprises();
    Entreprise getEntrepriseById(Long id);
    
    /**
     * Associe un domaine d'activité à une entreprise
     * @param entrepriseId ID de l'entreprise
     * @param domaineId ID du domaine d'activité
     * @return L'entreprise mise à jour
     */
    Entreprise associerDomaineActivite(Long entrepriseId, Long domaineId);
}