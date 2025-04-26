package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.entities.Employe;
import tn.fst.spring.netvoyage.entities.Voyage;
import tn.fst.spring.netvoyage.entities.Voyageur;

import java.time.LocalDate;
import java.util.List;

public interface IVoyageService {

    Voyage createVoyage(Voyage voyage, Long organizerId);
    Voyage getVoyageById(Long id);
    List<Voyage> getAllVoyages();
    Voyage updateVoyage(Long id, Voyage voyageDetails);
    void deleteVoyage(Long id);
    void ajouterParticipantAuVoyage(Long voyageId, Employe employe);

    void addParticipant(Voyage voyage, Voyageur voyageur);
    void removeParticipant(Voyage voyage, Voyageur voyageur);


    List<Voyage> getVoyagesParEntreprise(Long entrepriseId);

    List<Voyage> getVoyagesByCompany(Long companyId);

    /**
     * Recherche des voyages correspondant aux critères de matching
     * @param dateReference Date de référence pour le voyage
     * @param marge Marge en jours autour de la date de référence
     * @param destination Destination du voyage (peut être null)
     * @param perimetre Périmètre du voyage (peut être null)
     * @return Liste des voyages correspondant aux critères
     */
    List<Voyage> findMatchingVoyages(LocalDate dateReference, int marge, String destination, String perimetre);
    List<Voyage> findVoyagesByDestination(String destination);
}

