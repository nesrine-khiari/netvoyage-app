package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.dtos.ReclamationDTO;
import tn.fst.spring.netvoyage.dtos.ReclamationResponseDTO;
import tn.fst.spring.netvoyage.services.interfaces.IReclamationService;
import tn.fst.spring.netvoyage.entities.ReclamationStatus;
import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
@RequiredArgsConstructor
public class ReclamationController {

    private final IReclamationService service;

    // Créer une réclamation (accessible aux employés)
    @PostMapping
    public ResponseEntity<ReclamationResponseDTO> create(@RequestBody ReclamationDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }


    @GetMapping("/entreprise/{entrepriseId}")
    @PreAuthorize("@securityService.isUserFromCompany(#entrepriseId)")
    public ResponseEntity<List<ReclamationResponseDTO>> getByEntreprise(
            @PathVariable Long entrepriseId
    ) {
        return ResponseEntity.ok(service.getByEntreprise(entrepriseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReclamationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }


    @PatchMapping("/{id}/resolve")
    @PreAuthorize("@securityService.canManageReclamation(#id)")
    public ResponseEntity<ReclamationResponseDTO> markAsResolved(@PathVariable Long id) {
        return ResponseEntity.ok(service.markAsResolved(id));
    }

    @GetMapping("/entreprise/{entrepriseId}/status/{status}")
    @PreAuthorize("@securityService.isUserFromCompany(#entrepriseId)")
    public ResponseEntity<List<ReclamationResponseDTO>> getByStatusAndEntreprise(
            @PathVariable Long entrepriseId,
            @PathVariable ReclamationStatus status
    ) {
        return ResponseEntity.ok(service.getByStatusAndEntreprise(status, entrepriseId));
    }
}