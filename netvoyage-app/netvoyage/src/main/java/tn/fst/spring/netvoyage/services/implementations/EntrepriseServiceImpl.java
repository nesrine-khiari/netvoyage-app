package tn.fst.spring.netvoyage.services.implementations;

import tn.fst.spring.netvoyage.dtos.EntrepriseDTO;
import tn.fst.spring.netvoyage.entities.Entreprise;
import tn.fst.spring.netvoyage.entities.DomaineActivite;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.fst.spring.netvoyage.repositories.DomaineActiviteRepository;
import tn.fst.spring.netvoyage.repositories.EntrepriseRepository;
import tn.fst.spring.netvoyage.services.interfaces.EntrepriseService;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class EntrepriseServiceImpl implements EntrepriseService {

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private DomaineActiviteRepository domaineActiviteRepository;

    private final String UPLOAD_DIR = "uploads/logos/";

    @Override
    public Entreprise updateEntreprise(Long id, EntrepriseDTO dto, MultipartFile logo) throws IOException {
        Entreprise entreprise = entrepriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));

        if (dto.getNomEntreprise() != null) entreprise.setNomEntreprise(dto.getNomEntreprise());
        if (dto.getPays() != null) entreprise.setPays(dto.getPays());
        if (dto.getAdresse() != null) entreprise.setAdresse(dto.getAdresse());
        if (dto.getTelephone() != null) entreprise.setTelephone(dto.getTelephone());

        if (dto.getDomaineId() != null) {
            DomaineActivite domaine = domaineActiviteRepository.findById(dto.getDomaineId())
                    .orElseThrow(() -> new RuntimeException("Domaine non trouvé"));
            entreprise.setDomaine(domaine);
        }

        if (logo != null && !logo.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + logo.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR);
            if (!Files.exists(path)) Files.createDirectories(path);
            Files.copy(logo.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            entreprise.setLogoUrl("/" + UPLOAD_DIR + fileName);
        }

        return entrepriseRepository.save(entreprise);
    }

    @Override
    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }

    @Override
    public Entreprise getEntrepriseById(Long id) {
        return entrepriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
    }
    
    @Override
    public Entreprise associerDomaineActivite(Long entrepriseId, Long domaineId) {
        // Récupérer l'entreprise
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        
        // Récupérer le domaine d'activité
        DomaineActivite domaine = domaineActiviteRepository.findById(domaineId)
                .orElseThrow(() -> new RuntimeException("Domaine d'activité non trouvé"));
        
        // Associer le domaine à l'entreprise
        entreprise.setDomaine(domaine);
        
        // Sauvegarder et retourner l'entreprise mise à jour
        return entrepriseRepository.save(entreprise);
    }
}
