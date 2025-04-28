package tn.fst.spring.netvoyage.services.interfaces;

import tn.fst.spring.netvoyage.dtos.ReclamationDTO;
import tn.fst.spring.netvoyage.dtos.ReclamationResponseDTO;
import tn.fst.spring.netvoyage.entities.ReclamationStatus;
import java.util.List;

public interface IReclamationService {
    ReclamationResponseDTO create(ReclamationDTO dto);
    List<ReclamationResponseDTO> getByEntreprise(Long entrepriseId);
    ReclamationResponseDTO markAsResolved(Long id);
    List<ReclamationResponseDTO> getByStatusAndEntreprise(ReclamationStatus status, Long entrepriseId);
    ReclamationResponseDTO getById(Long id);

    long countTotalReclamations();
    long countReclamationsByStatus(ReclamationStatus status);
    long countReclamationsByEntreprise(Long entrepriseId);
    long countResolvedReclamationsByEntreprise(Long entrepriseId);
    long countInProgressReclamationsByEntreprise(Long entrepriseId);
}