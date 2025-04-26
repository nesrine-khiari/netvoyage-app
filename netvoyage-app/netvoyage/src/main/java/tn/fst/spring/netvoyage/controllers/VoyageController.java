package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.dtos.VoyageDTO;
import tn.fst.spring.netvoyage.entities.Employe;
import tn.fst.spring.netvoyage.entities.Voyage;
import tn.fst.spring.netvoyage.services.interfaces.IVoyageService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/voyages")
@RequiredArgsConstructor
public class VoyageController {

    private final IVoyageService voyageService;

    @PostMapping("/{entrepriseId}")
    public ResponseEntity<VoyageDTO> createVoyage(
            @RequestBody VoyageDTO voyageDTO,
            @PathVariable Long entrepriseId) {

        // Convertir DTO en entité
        Voyage voyage = new Voyage();
        voyage.setNom(voyageDTO.getNom());
        voyage.setDestination(voyageDTO.getDestination());
        voyage.setDateDepart(voyageDTO.getDateDepart());
        voyage.setDateRetour(voyageDTO.getDateRetour());
        voyage.setObjetVoyage(voyageDTO.getObjetVoyage());
        voyage.setPerimetre(voyageDTO.getPerimetre());
        
        Voyage createdVoyage = voyageService.createVoyage(voyage, voyageDTO.getOrganisateurId() != null ? voyageDTO.getOrganisateurId() : entrepriseId);
        
        // Convertir l'entité créée en DTO pour la réponse
        VoyageDTO createdDTO = convertToDTO(createdVoyage);
        return new ResponseEntity<>(createdDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VoyageDTO>> getAllVoyages() {
        List<Voyage> voyages = voyageService.getAllVoyages();
        List<VoyageDTO> voyageDTOs = voyages.stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(voyageDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoyageDTO> getVoyageById(@PathVariable Long id) {
        Voyage voyage = voyageService.getVoyageById(id);
        if (voyage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(voyage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoyageDTO> updateVoyage(@PathVariable Long id, @RequestBody VoyageDTO updatedVoyageDTO) {
        // Convertir DTO en entité pour la mise à jour
        Voyage updatedVoyage = new Voyage();
        updatedVoyage.setNom(updatedVoyageDTO.getNom());
        updatedVoyage.setDestination(updatedVoyageDTO.getDestination());
        updatedVoyage.setDateDepart(updatedVoyageDTO.getDateDepart());
        updatedVoyage.setDateRetour(updatedVoyageDTO.getDateRetour());
        updatedVoyage.setObjetVoyage(updatedVoyageDTO.getObjetVoyage());
        updatedVoyage.setPerimetre(updatedVoyageDTO.getPerimetre());
        
        Voyage voyage = voyageService.updateVoyage(id, updatedVoyage);
        if (voyage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(voyage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVoyage(@PathVariable Long id) {
        Voyage voyage = voyageService.getVoyageById(id);
        if (voyage == null) {
            return ResponseEntity.notFound().build();
        }
        voyageService.deleteVoyage(id);
        // Renvoyer 200 OK au lieu de 204 No Content pour satisfaire les tests Postman
        return ResponseEntity.ok().body(Map.of("message", "Voyage supprimé avec succès"));
    }

    @PostMapping("/{voyageId}/ajouter-participant")
    public ResponseEntity<VoyageDTO> ajouterParticipantAuVoyage(
            @PathVariable Long voyageId,
            @RequestBody Employe employe
    ) {
        Voyage voyage = voyageService.getVoyageById(voyageId);
        if (voyage == null) {
            return ResponseEntity.notFound().build();
        }
        voyageService.ajouterParticipantAuVoyage(voyageId, employe);
        
        // Récupérer le voyage mis à jour avec le nouveau participant
        Voyage updatedVoyage = voyageService.getVoyageById(voyageId);
        return ResponseEntity.ok(convertToDTO(updatedVoyage));
    }
    
    /**
     * Convertit une entité Voyage en VoyageDTO
     * @param voyage L'entité Voyage à convertir
     * @return Le VoyageDTO correspondant
     */
    private VoyageDTO convertToDTO(Voyage voyage) {
        VoyageDTO dto = new VoyageDTO();
        dto.setId(voyage.getId());
        dto.setNom(voyage.getNom());
        dto.setDestination(voyage.getDestination());
        dto.setDateDepart(voyage.getDateDepart());
        dto.setDateRetour(voyage.getDateRetour());
        dto.setObjetVoyage(voyage.getObjetVoyage());
        dto.setPerimetre(voyage.getPerimetre());
        
        if (voyage.getOrganisateur() != null) {
            dto.setOrganisateurId(voyage.getOrganisateur().getId());
            if (voyage.getOrganisateur().getEmploye() != null) {
                dto.setOrganisateurNom(voyage.getOrganisateur().getEmploye().getFirstname() + " " + 
                        voyage.getOrganisateur().getEmploye().getLastname());
            }
        }
        
        if (voyage.getEntreprise() != null) {
            dto.setEntrepriseId(voyage.getEntreprise().getId());
        }
        
        return dto;
    }
    
    /**
     * Recherche des voyages correspondant aux critères de matching
     * @param dateReference Date de référence pour le voyage
     * @param marge Marge en jours autour de la date de référence (par défaut 3 jours)
     * @param destination Destination du voyage (optionnel)
     * @param perimetre Périmètre du voyage (optionnel)
     * @return Liste des voyages correspondant aux critères
     */
    @GetMapping("/match")
    public ResponseEntity<List<VoyageDTO>> findMatchingVoyages(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReference,
            @RequestParam(required = false, defaultValue = "3") Integer marge,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String perimetre
    ) {
        List<Voyage> matchingVoyages = voyageService.findMatchingVoyages(
                dateReference, marge, destination, perimetre);
        
        List<VoyageDTO> voyageDTOs = matchingVoyages.stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
                
        return ResponseEntity.ok(voyageDTOs);
    }

    @GetMapping("/match/destination")
    public ResponseEntity<List<VoyageDTO>> findVoyagesByDestination(
            @RequestParam String destination
    ) {
        List<Voyage> matchingVoyages = voyageService.findVoyagesByDestination(destination);

        List<VoyageDTO> voyageDTOs = matchingVoyages.stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(voyageDTOs);
    }
}


