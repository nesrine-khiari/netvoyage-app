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

        String authorFullName = (reclamation.getAuthor() != null && reclamation.getAuthor().getEmploye() != null) ?
                reclamation.getAuthor().getEmploye().getFirstname() + " " + reclamation.getAuthor().getEmploye().getLastname()
                : "Inconnu";

        String targetFullName = (reclamation.getTarget() != null && reclamation.getTarget().getEmploye() != null) ?
                reclamation.getTarget().getEmploye().getFirstname() + " " + reclamation.getTarget().getEmploye().getLastname()
                : "Inconnu";

        return ReclamationResponseDTO.builder()
                .id(reclamation.getNumReclamation())
                .titre(reclamation.getTitre())
                .description(reclamation.getDescription())
                .status(reclamation.getStatus())
                .authorId(reclamation.getAuthor() != null ? reclamation.getAuthor().getId() : null)
                .authorName(authorFullName)
                .targetId(reclamation.getTarget() != null ? reclamation.getTarget().getId() : null)
                .targetName(targetFullName)
                .voyageId(reclamation.getVoyage() != null ? reclamation.getVoyage().getId() : null)
                .build();
    }

    @Override
    public ReclamationResponseDTO create(ReclamationDTO dto) {
        Voyageur author = voyageurRepo.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Auteur non trouvé"));
        Voyageur target = voyageurRepo.findById(dto.getTargetId())
                .orElseThrow(() -> new RuntimeException("Cible non trouvée"));

        if (dto.getVoyageId() != null) {
            Voyage voyage = voyageRepo.findById(dto.getVoyageId())
                    .orElseThrow(() -> new RuntimeException("Voyage non trouvé"));

            if (!voyage.getParticipants().contains(author) || !voyage.getParticipants().contains(target)) {
                throw new RuntimeException("L'auteur et la cible n'ont pas voyagé ensemble");
            }
        }

        Reclamation reclamation = new Reclamation();
        reclamation.setTitre(dto.getTitre());
        reclamation.setDescription(dto.getDescription());
        reclamation.setAuthor(author);
        reclamation.setTarget(target);
        reclamation.setVoyage(dto.getVoyageId() != null ?
                voyageRepo.findById(dto.getVoyageId()).orElse(null) : null);
        reclamation.setStatus(ReclamationStatus.IN_PROGRESS);

        Reclamation savedReclamation = reclamationRepo.save(reclamation);
        return mapToDTO(savedReclamation);
    }

    @Override
    public List<ReclamationResponseDTO> getByEntreprise(Long entrepriseId) {
        List<Reclamation> reclamations = reclamationRepo.findByAuthor_Employe_Entreprise_Id(entrepriseId);
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
        return reclamationRepo.findByStatusAndAuthor_Employe_Entreprise_Id(status, entrepriseId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countTotalReclamations() {
        return reclamationRepo.count();
    }

    @Override
    public long countReclamationsByStatus(ReclamationStatus status) {
        return reclamationRepo.countByStatus(status);
    }

    @Override
    public long countReclamationsByEntreprise(Long entrepriseId) {
        return reclamationRepo.countByAuthor_Employe_Entreprise_Id(entrepriseId);
    }

    @Override
    public long countResolvedReclamationsByEntreprise(Long entrepriseId) {
        return reclamationRepo.countByStatusAndAuthor_Employe_Entreprise_Id(ReclamationStatus.RESOLVED, entrepriseId);
    }

    @Override
    public long countInProgressReclamationsByEntreprise(Long entrepriseId) {
        return reclamationRepo.countByStatusAndAuthor_Employe_Entreprise_Id(ReclamationStatus.IN_PROGRESS, entrepriseId);
    }
}