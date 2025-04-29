package tn.fst.spring.netvoyage.services.implementations;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.entities.*;
import tn.fst.spring.netvoyage.repositories.DiscussionRepository;
import tn.fst.spring.netvoyage.repositories.EntrepriseRepository;
import tn.fst.spring.netvoyage.repositories.VoyageRepository;
import tn.fst.spring.netvoyage.repositories.VoyageurRepository;
import tn.fst.spring.netvoyage.services.interfaces.IVoyageService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VoyageServiceImpl implements IVoyageService {

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private VoyageurRepository voyageurRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Override
    public void affecterEmployeAVoyage(Employe employe, Voyage voyage) {
        // Vérifier si l'employé est déjà un voyageur
        Optional<Voyageur> voyageurOpt = voyageurRepository.findByEmploye(employe);

        // Si l'employé n'est pas encore un voyageur, on le crée
        Voyageur voyageur = voyageurOpt.orElseGet(() -> {
            Voyageur newVoyageur = new Voyageur();
            newVoyageur.setEmploye(employe);
            return voyageurRepository.save(newVoyageur);  // Sauvegarde du voyageur créé
        });

        // Ajouter le voyageur au voyage (participants)
        voyage.addParticipant(voyageur);

        // Vérifier si le voyage a une discussion
        Discussion discussion = voyage.getDiscussion();
        if (discussion != null) {
            // Ajouter le voyageur à la discussion existante
            discussion.getParticipants().add(voyageur);

            // Sauvegarder la discussion avec le voyageur ajouté
            discussionRepository.save(discussion);
        } else {
            // Si le voyage n'a pas de discussion, créer une nouvelle discussion
            discussion = new Discussion();
            discussion.setVoyage(voyage);
            discussion.setName("Discussion for " + voyage.getNom());  // Nom de la discussion
            discussion.setMessages(new HashSet<>());  // Initialiser la liste des messages
            discussion.getParticipants().add(voyageur);  // Ajouter le voyageur à la nouvelle discussion

            // Sauvegarder la nouvelle discussion
            discussionRepository.save(discussion);

            // Associer la discussion au voyage
            voyage.setDiscussion(discussion);
        }

        // Sauvegarder les modifications du voyage (ajout du participant et de la discussion)
        voyageRepository.save(voyage);
    }


    @Override
    public Voyage createVoyage(Voyage voyage, Long organizerId) {
        // Récupérer l'organisateur par son ID
        Entreprise organizer = entrepriseRepository.findById(organizerId)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        // Affecter l'organisateur au voyage
        voyage.setOrganisateur(organizer);

        // Récupérer l'entreprise associée à l'organisateur et l'affecter au voyage
        if (organizer != null) {
            voyage.setEntreprise(organizer);
        } else {
            throw new RuntimeException("The organizer does not have an associated company.");
        }

        // Vérifier si l'entreprise a un domaine d'activité défini
        if (voyage.getEntreprise().getDomaine() == null) {
            System.out.println("Warning: The company does not have a defined business domain!");
            // Vous pouvez choisir de lever une exception si cela est critique pour le business logic
            // throw new RuntimeException("The company does not have a business domain defined.");
        }

        // Créer une nouvelle discussion pour le voyage
        Discussion discussion = new Discussion();
        discussion.setVoyage(voyage); // Associer la discussion au voyage
        discussion.setName("Discussion for " + voyage.getNom()); // Vous pouvez personnaliser le nom de la discussion
        discussion.setMessages(new HashSet<>()); // Si nécessaire, initialiser la liste des messages

        // Sauvegarder la discussion dans la base de données
        discussionRepository.save(discussion);

        // Associer la discussion au voyage
        voyage.setDiscussion(discussion);

        // Sauvegarder le voyage et retourner l'entité créée
        return voyageRepository.save(voyage);
    }


    @Override
    public List<Voyage> getVoyagesParEntreprise(Long entrepriseId) {
        return voyageRepository.findByEntrepriseId(entrepriseId);
    }

    @Override
    public Voyage getVoyageById(Long id) {
        return voyageRepository.findById(id).orElse(null);
    }

    @Override
    public List<Voyage> getAllVoyages() {
        return voyageRepository.findAll();
    }

    @Override
    public Voyage updateVoyage(Long id, Voyage updatedDetails) {
        Optional<Voyage> optional = voyageRepository.findById(id);
        if (optional.isPresent()) {
            Voyage voyage = optional.get();
            // Ajouter la mise à jour du nom du voyage
            if (updatedDetails.getNom() != null) {
                voyage.setNom(updatedDetails.getNom());
            }
            voyage.setDestination(updatedDetails.getDestination());
            voyage.setDateDepart(updatedDetails.getDateDepart());
            voyage.setDateRetour(updatedDetails.getDateRetour());
            voyage.setObjetVoyage(updatedDetails.getObjetVoyage());
            voyage.setPerimetre(updatedDetails.getPerimetre());
            return voyageRepository.save(voyage);
        }
        return null;
    }

    @Override
    public void deleteVoyage(Long id) {
        voyageRepository.deleteById(id);
    }

    @Override
    public void addParticipant(Voyage voyage, Voyageur voyageur) {
        voyage.addParticipant(voyageur);
        voyageRepository.save(voyage);
    }

    @Override
    public void removeParticipant(Voyage voyage, Voyageur voyageur) {
        voyage.removeParticipant(voyageur);
        voyageRepository.save(voyage);
    }

    @Override
    public List<Voyage> getVoyagesByCompany(Long companyId) {
        return voyageRepository.findAllByEntreprise_Id(companyId);
    }

    @Override
    public void ajouterParticipantAuVoyage(Long voyageId, Employe employe) {
        Voyage voyage = voyageRepository.findById(voyageId)
                .orElseThrow(() -> new RuntimeException("Voyage introuvable"));

        Voyageur voyageur = voyageurRepository.findByEmploye(employe)
                .orElseGet(() -> {
                    // Convertir l’employé en voyageur (sans supprimer l'employé)
                    Voyageur v = new Voyageur();
                    v.setEmploye(employe); // lien vers l'employé
                    return voyageurRepository.save(v);
                });

        voyage.addParticipant(voyageur);
        voyageRepository.save(voyage);
    }


    /**
     * Recherche des voyages correspondant aux critères de matching
     *
     * @param dateReference Date de référence pour le voyage
     * @param marge         Marge en jours autour de la date de référence
     * @param destination   Destination du voyage (peut être null)
     * @param perimetre     Périmètre du voyage (peut être null)
     * @return Liste des voyages correspondant aux critères
     */
    @Override
    public List<Voyage> findMatchingVoyages(LocalDate dateReference, int marge, String destination, String perimetre) {
        // Calcul des dates avec la marge
        LocalDate dateDepart = dateReference.minusDays(marge);
        LocalDate dateRetour = dateReference.plusDays(marge);

        System.out.println("=== Début recherche de voyages correspondants ===");
        System.out.println("Date de référence: " + dateReference);
        System.out.println("Marge: " + marge + " jours");
        System.out.println("Date de départ min: " + dateDepart);
        System.out.println("Date de retour max: " + dateRetour);
        System.out.println("Destination: " + (destination != null ? destination : "non spécifiée"));
        System.out.println("Périmètre: " + (perimetre != null ? perimetre : "non spécifié"));

        // Vérification des voyages disponibles dans la base de données
        List<Voyage> allVoyages = voyageRepository.findAll();
        System.out.println("Nombre total de voyages dans la base: " + allVoyages.size());
        for (Voyage v : allVoyages) {
            System.out.println("Voyage ID: " + v.getId() + ", Nom: " + v.getNom() + ", Destination: " + v.getDestination() + ", Date départ: " + v.getDateDepart());
            if (v.getEntreprise() != null && v.getEntreprise().getDomaine() != null) {
                System.out.println("   Entreprise: " + v.getEntreprise().getNomEntreprise() + ", Domaine ID: " + v.getEntreprise().getDomaine().getId());
            } else {
                System.out.println("   Entreprise ou domaine non défini pour ce voyage");
            }
        }

        // Recherche des voyages correspondant aux critères
        List<Voyage> matchingVoyages = voyageRepository.findMatchingVoyages(
                dateDepart,
                dateRetour,
                destination,
                perimetre
        );

        System.out.println("Nombre de voyages correspondants trouvés: " + matchingVoyages.size());
        System.out.println("=== Fin recherche de voyages correspondants ===");

        return matchingVoyages;
    }


    public List<Voyage> findVoyagesByDestination(String destination) {
        System.out.println("=== Début recherche de voyages par destination ===");

        // Vérification des voyages disponibles dans la base de données
        List<Voyage> allVoyages = voyageRepository.findAll();
        System.out.println("Nombre total de voyages dans la base: " + allVoyages.size());

        // Filtrage des voyages correspondant à la destination
        List<Voyage> matchingVoyages = new ArrayList<>();
        for (Voyage v : allVoyages) {
            if (v.getDestination() != null && v.getDestination().equalsIgnoreCase(destination)) {
                matchingVoyages.add(v);
            }
        }

        System.out.println("Nombre de voyages correspondants trouvés: " + matchingVoyages.size());
        System.out.println("=== Fin recherche de voyages par destination ===");

        return matchingVoyages;
    }

    //ban voyageur
    public void banVoyageur(Long voyageurId) {
        Voyageur voyageur = voyageurRepository.findById(voyageurId)
                .orElseThrow(() -> new RuntimeException("Voyageur not found"));
        voyageur.setBanned(true); // you need a 'banned' boolean field
        voyageurRepository.save(voyageur);
    }

}



