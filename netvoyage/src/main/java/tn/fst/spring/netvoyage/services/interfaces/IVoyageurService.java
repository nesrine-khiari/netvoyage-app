package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.entities.Voyageur;
import java.util.List;
import java.util.Optional;

public interface IVoyageurService {
    Voyageur saveVoyageur(Voyageur voyageur);
    List<Voyageur> getAllVoyageurs();
    Optional<Voyageur> getVoyageurById(Long id);
    void deleteVoyageur(Long id);
}
