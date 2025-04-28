package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.dtos.VoyageDTO;
import tn.fst.spring.netvoyage.entities.Employe;
import tn.fst.spring.netvoyage.entities.Voyage;
import tn.fst.spring.netvoyage.entities.Voyageur;
import tn.fst.spring.netvoyage.repositories.EmployeRepository;
import tn.fst.spring.netvoyage.repositories.VoyageRepository;
import tn.fst.spring.netvoyage.repositories.VoyageurRepository;
import tn.fst.spring.netvoyage.services.interfaces.IVoyageService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/voyages")
@RequiredArgsConstructor
public class VoyageController {

    private final IVoyageService voyageService;
    private final VoyageRepository voyageRepository;
    private final VoyageurRepository voyageurRepository;
    private final EmployeRepository employeRepository;


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
    @PostMapping("/{voyageId}/affecter-employe/{employeId}")
    public ResponseEntity<String> affecterEmployeAVoyage(
            @PathVariable Long voyageId, @PathVariable Long employeId) {

        // Vérifier si le voyage existe
        Optional<Voyage> optionalVoyage = voyageRepository.findById(voyageId);

        if (optionalVoyage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voyage non trouvé.");
        }

        Voyage voyage = optionalVoyage.get();

        // Vérifier si l'employé existe
        Optional<Employe> optionalEmploye = employeRepository.findById(employeId);

        if (optionalEmploye.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employé non trouvé.");
        }

        Employe employe = optionalEmploye.get();

        // Appeler le service pour affecter l'employé au voyage
        try {
            voyageService.affecterEmployeAVoyage(employe, voyage);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Employé affecté au voyage avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de l'affectation de l'employé au voyage.");
        }
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

    // Associer un participant (voyageur) à un voyage
    @PostMapping("/{voyageId}/add-participant/{voyageurId}")
    public ResponseEntity<?> addParticipantToVoyage(@PathVariable Long voyageId, @PathVariable Long voyageurId) {
        var voyageOpt = voyageRepository.findById(voyageId);
        var voyageurOpt = voyageurRepository.findById(voyageurId);
        if (voyageOpt.isPresent() && voyageurOpt.isPresent()) {
            Voyage voyage = voyageOpt.get();
            Voyageur voyageur = voyageurOpt.get();
            voyage.addParticipant(voyageur);
            voyageRepository.save(voyage);
            return ResponseEntity.ok().body("Participant ajouté au voyage.");
        }
        return ResponseEntity.notFound().build();
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
            if (voyage.getOrganisateur().getId() != null) {
                dto.setOrganisateurNom(voyage.getOrganisateur().getNomEntreprise());
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
}