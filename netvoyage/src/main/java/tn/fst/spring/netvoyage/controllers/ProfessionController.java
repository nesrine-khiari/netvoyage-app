package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.dtos.ProfessionStatsDTO;
import tn.fst.spring.netvoyage.entities.Profession;
import tn.fst.spring.netvoyage.services.interfaces.ProfessionService;

import java.util.List;

@RestController
@RequestMapping("/api/professions")
@RequiredArgsConstructor
public class ProfessionController {

    private final ProfessionService professionService;

    // Récupère toutes les professions
    @GetMapping
    public ResponseEntity<List<Profession>> getAll() {
        List<Profession> professions = professionService.getAll();
        if (professions.isEmpty()) {
            return ResponseEntity.noContent().build();  // Si aucune profession n'est trouvée
        }
        return ResponseEntity.ok(professions);
    }

    // Récupère une profession par son identifiant
    @GetMapping("/{id}")
    public ResponseEntity<Profession> getById(@PathVariable Long id) {
        return professionService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());  // Si la profession n'est pas trouvée
    }

    // Ajoute une nouvelle profession
    @PostMapping
    public ResponseEntity<Profession> addProfession(@RequestBody Profession profession) {
        // Assure-toi que le corps de la requête est bien au format JSON
        Profession savedProfession = professionService.addProfession(profession);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfession);
    }

    // Met à jour une profession existante
    @PutMapping("/{id}")
    public ResponseEntity<Profession> update(@PathVariable Long id, @RequestBody Profession updatedProfession) {
        // Vérifie si la profession existe avant la mise à jour
        return professionService.getById(id)
                .map(existingProfession -> {
                    Profession updated = professionService.update(id, updatedProfession);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());  // Si la profession n'existe pas
    }

    // Supprime une profession
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (professionService.getById(id).isPresent()) {
            professionService.delete(id);
            return ResponseEntity.noContent().build();  // Profession supprimée
        }
        return ResponseEntity.notFound().build();  // Si la profession n'est pas trouvée
    }



    @GetMapping("/stats")
    public List<ProfessionStatsDTO> getProfessionStats() {
        return professionService.getStats();
    }


}
