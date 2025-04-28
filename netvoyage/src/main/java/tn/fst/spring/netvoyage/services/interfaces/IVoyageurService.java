package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.entities.Voyageur;
import java.util.List;
import java.util.Optional;

public interface IVoyageurService {
    Voyageur saveVoyageur(Voyageur voyageur);
    List<Voyageur> getAllVoyageurs();
    Optional<Voyageur> getVoyageurById(Long id);
    void deleteVoyageur(Long id);
    // Method to check if a Voyageur is banned
    boolean isBanned(Long voyageurId);

    // Method to ban a Voyageur
    void banVoyageur(Long voyageurId);

    // Method to unban a Voyageur
    void unbanVoyageur(Long voyageurId);

}