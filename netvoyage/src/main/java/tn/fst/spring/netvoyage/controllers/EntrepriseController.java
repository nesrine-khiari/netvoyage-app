package tn.fst.spring.netvoyage.controllers;

import tn.fst.spring.netvoyage.dtos.EntrepriseDTO;
import tn.fst.spring.netvoyage.entities.Entreprise;
import tn.fst.spring.netvoyage.services.interfaces.EntrepriseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseController {

    @Autowired
    private EntrepriseService entrepriseService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEntreprise(
            @PathVariable Long id,
            @ModelAttribute EntrepriseDTO entrepriseDTO,
            @RequestParam(value = "logo", required = false) MultipartFile logo
    ) {
        try {
            Entreprise updated = entrepriseService.updateEntreprise(id, entrepriseDTO, logo);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Entreprise>> getAllEntreprises() {
        return ResponseEntity.ok(entrepriseService.getAllEntreprises());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEntrepriseById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(entrepriseService.getEntrepriseById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entreprise non trouvée");
        }
    }
}
