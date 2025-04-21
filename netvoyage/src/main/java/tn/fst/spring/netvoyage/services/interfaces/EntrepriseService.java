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
}
