package tn.fst.spring.netvoyage.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.Voyageur;
import tn.fst.spring.netvoyage.repositories.VoyageurRepository;
import tn.fst.spring.netvoyage.services.interfaces.IVoyageurService;

import java.util.List;
import java.util.Optional;

@Service
public class VoyageurServiceImpl implements IVoyageurService {

    @Autowired
    private VoyageurRepository voyageurRepository;

    @Override
    public Voyageur saveVoyageur(Voyageur voyageur) {
        return voyageurRepository.save(voyageur);
    }

    @Override
    public List<Voyageur> getAllVoyageurs() {
        return voyageurRepository.findAll();
    }

    @Override
    public Optional<Voyageur> getVoyageurById(Long id) {
        return voyageurRepository.findById(id);
    }

    @Override
    public void deleteVoyageur(Long id) {
        voyageurRepository.deleteById(id);
    }

    // Check if the Voyageur is banned
    @Override
    public boolean isBanned(Long voyageurId) {
        Optional<Voyageur> voyageur = voyageurRepository.findById(voyageurId);
        return voyageur.map(Voyageur::isBanned).orElse(false);
    }

    // Ban a Voyageur
    @Override
    public void banVoyageur(Long voyageurId) {
        Optional<Voyageur> voyageurOpt = voyageurRepository.findById(voyageurId);
        voyageurOpt.ifPresent(voyageur -> {
            voyageur.setBanned(true);
            voyageurRepository.save(voyageur);  // Save the updated status
        });
    }

    // Unban a Voyageur
    @Override
    public void unbanVoyageur(Long voyageurId) {
        Optional<Voyageur> voyageurOpt = voyageurRepository.findById(voyageurId);
        voyageurOpt.ifPresent(voyageur -> {
            voyageur.setBanned(false);
            voyageurRepository.save(voyageur);  // Save the updated status
        });
    }
}
