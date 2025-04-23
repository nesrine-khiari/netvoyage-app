package tn.fst.spring.netvoyage.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.fst.spring.netvoyage.dtos.ReclamationDTO;
import tn.fst.spring.netvoyage.dtos.ReclamationResponseDTO;
import tn.fst.spring.netvoyage.entities.*;
import tn.fst.spring.netvoyage.repositories.ReclamationRepository;
import tn.fst.spring.netvoyage.repositories.VoyageRepository;
import tn.fst.spring.netvoyage.repositories.VoyageurRepository;
import tn.fst.spring.netvoyage.services.interfaces.IReclamationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReclamationService implements IReclamationService {

    private final ReclamationRepository reclamationRepo;
    private final VoyageurRepository voyageurRepo;
    private final VoyageRepository voyageRepo;

    private ReclamationResponseDTO mapToDTO(Reclamation reclamation) {
        if (reclamation == null) return null;

        String authorFullName = (reclamation.getAuthor() != null) ?
                reclamation.getAuthor().getFirstname() + " " + reclamation.getAuthor().getLastname()
                : "Inconnu";

        String targetFullName = (reclamation.getTarget() != null) ?
                reclamation.getTarget().getFirstname() + " " + reclamation.getTarget().getLastname()
                : "Inconnu";

        return ReclamationResponseDTO.builder()
                .id(reclamation.getNumReclamation())
                .titre(reclamation.getTitre())
                .description(reclamation.getDescription())
                .status(reclamation.getStatus())
                .authorId(reclamation.getAuthor().getNumVoyageur())
                .authorName(authorFullName)
                .targetId(reclamation.getTarget().getNumVoyageur())
                .targetName(targetFullName)
                .voyageId(reclamation.getVoyage() != null ? reclamation.getVoyage().getNumVoyage() : null)
                .build();
    }

    @Override
    public ReclamationResponseDTO create(ReclamationDTO dto) {
        // Validation des entités
        Voyageur author = voyageurRepo.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé"));
        Voyageur target = voyageurRepo.findById(dto.getTargetId())
                .orElseThrow(() -> new RuntimeException("Cible non trouvée"));

        // Vérification du voyage commun
        if (dto.getVoyageId() != null) {
            Voyage voyage = voyageRepo.findById(dto.getVoyageId())
                    .orElseThrow(() -> new RuntimeException("Voyage non trouvé"));

            if (!voyage.getVoyageurs().contains(author) || !voyage.getVoyageurs().contains(target)) {
                throw new RuntimeException("L'auteur et la cible n'ont pas voyagé ensemble");
            }
        }

        // Création de la réclamation
        Reclamation reclamation = new Reclamation();
        reclamation.setTitre(dto.getTitre());
        reclamation.setDescription(dto.getDescription());
        reclamation.setAuthor(author);
        reclamation.setTarget(target);
        reclamation.setVoyage(dto.getVoyageId() != null ?
                voyageRepo.findById(dto.getVoyageId()).orElse(null) : null);
        reclamation.setStatus(ReclamationStatus.IN_PROGRESS); // Statut initial

        Reclamation savedReclamation = reclamationRepo.save(reclamation);
        return mapToDTO(savedReclamation);
    }

    @Override
    public List<ReclamationResponseDTO> getByEntreprise(Long entrepriseId) {
        List<Reclamation> reclamations = reclamationRepo.findByAuthor_Entreprise_NumEntreprise(entrepriseId);
        return reclamations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }



    @Override
    public ReclamationResponseDTO getById(Long id) {
        return reclamationRepo.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public ReclamationResponseDTO markAsResolved(Long reclamationId) {
        Reclamation reclamation = reclamationRepo.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));

        reclamation.setStatus(ReclamationStatus.RESOLVED);
        Reclamation updatedReclamation = reclamationRepo.save(reclamation);

        return mapToDTO(updatedReclamation);
    }

    @Override
    public List<ReclamationResponseDTO> getByStatusAndEntreprise(ReclamationStatus status, Long entrepriseId) {
        return reclamationRepo.findByStatusAndAuthor_Entreprise_NumEntreprise(status, entrepriseId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}