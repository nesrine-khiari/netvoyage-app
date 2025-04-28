package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.dtos.ReclamationDTO;
import tn.fst.spring.netvoyage.dtos.ReclamationResponseDTO;
import tn.fst.spring.netvoyage.services.interfaces.IReclamationService;
import tn.fst.spring.netvoyage.services.implementations.EntrepriseService;
import tn.fst.spring.netvoyage.services.implementations.VoyageurServiceImpl;
import tn.fst.spring.netvoyage.services.implementations.ReclamationService;
import tn.fst.spring.netvoyage.entities.ReclamationStatus;
import tn.fst.spring.netvoyage.entities.Voyageur;
import tn.fst.spring.netvoyage.repositories.VoyageurRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import tn.fst.spring.netvoyage.utils.JWTUtils;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/reclamations")
@RequiredArgsConstructor
public class ReclamationController {

    private final IReclamationService service;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private EntrepriseService entrepriseService;
    @Autowired
    private VoyageurServiceImpl voyageurService;


    @Autowired
    private ReclamationService reclamationService;

    @Autowired
    private VoyageurRepository voyageurRepository;

    @PostMapping
    public ResponseEntity<ReclamationResponseDTO> create(@RequestBody ReclamationDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }


    private Long validateAndGetEntrepriseId(Long requestedEntrepriseId, String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Token manquant ou mal formaté");
        }
        String token = authHeader.substring(7);
        String email = jwtUtils.getUsernameFromToken(token);
        Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email);

        // si on a un ID en path, on le compare
        if (requestedEntrepriseId != null && !requestedEntrepriseId.equals(entrepriseIdFromToken)) {
            throw new SecurityException("Accès non autorisé à cette entreprise");
        }
        return entrepriseIdFromToken;
    }



    @GetMapping("/entreprise/{entrepriseId}")
    public ResponseEntity<?> getByEntreprise(
            @PathVariable Long entrepriseId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            String email = jwtUtils.getUsernameFromToken(token);

            Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email);

            if (!entrepriseId.equals(entrepriseIdFromToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Accès refusé à cette entreprise.");
            }

            return ResponseEntity.ok(service.getByEntreprise(entrepriseId));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur : " + e.getMessage());
        }
    }


    @PatchMapping("/{id}/resolve")
    public ResponseEntity<?> markAsResolved(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            // 1. Vérification du token
            String token = authHeader.substring(7);
            String email = jwtUtils.getUsernameFromToken(token);
            Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email);

            // 2. Vérification que la réclamation appartient à l'entreprise
            ReclamationResponseDTO reclamation = service.getById(id);
            if (reclamation == null) {
                return ResponseEntity.notFound().build();
            }

            // 3. Vérification via la méthode existante getByEntreprise
            List<ReclamationResponseDTO> entrepriseReclamations = service.getByEntreprise(entrepriseIdFromToken);
            boolean hasAccess = entrepriseReclamations.stream()
                    .anyMatch(r -> r.getId().equals(id));

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Accès non autorisé à cette réclamation");
            }

            // 4. Traitement
            return ResponseEntity.ok(service.markAsResolved(id));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }
    @GetMapping("/entreprise/{entrepriseId}/status/{status}")
    @PreAuthorize("@securityService.isUserFromCompany(#entrepriseId)") // Sécurité existante
    public ResponseEntity<List<ReclamationResponseDTO>> getByStatusAndEntreprise(
            @PathVariable Long entrepriseId,
            @PathVariable ReclamationStatus status,
            @RequestHeader("Authorization") String authHeader // Token ajouté
    ) {
        // 1. Vérification du token (sans utiliser getEntrepriseIdByReclamationId)
        String token = authHeader.substring(7); // Enlève "Bearer "
        String email = jwtUtils.getUsernameFromToken(token);
        Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email); // Utilise un service EXISTANT

        // 2. Compare avec l'entreprise demandée
        if (!entrepriseId.equals(entrepriseIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Accès refusé
        }

        // 3. Retourne le résultat original (pas de modification du service)
        return ResponseEntity.ok(service.getByStatusAndEntreprise(status, entrepriseId));
    }


    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalReclamations() {
        return ResponseEntity.ok(service.countTotalReclamations());
    }

    @GetMapping("/stats/total/{status}")
    public ResponseEntity<Long> getTotalByStatus(@PathVariable ReclamationStatus status) {
        return ResponseEntity.ok(service.countReclamationsByStatus(status));
    }


    @GetMapping("/stats/entreprise/{entrepriseId}")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<Long> getTotalByEntreprise(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long entrepriseId) {

        Long validEnt = validateAndGetEntrepriseId(entrepriseId, authHeader);
        long total = service.countReclamationsByEntreprise(validEnt);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/stats/entreprise/{entrepriseId}/resolved")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<Long> getResolvedByEntreprise(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long entrepriseId) {

        Long validEnt = validateAndGetEntrepriseId(entrepriseId, authHeader);
        long resolved = service.countResolvedReclamationsByEntreprise(validEnt);
        return ResponseEntity.ok(resolved);
    }

    @GetMapping("/stats/entreprise/{entrepriseId}/in_progress")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<Long> getInProgressByEntreprise(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long entrepriseId) {

        Long validEnt = validateAndGetEntrepriseId(entrepriseId, authHeader);
        long inProgress = service.countInProgressReclamationsByEntreprise(validEnt);
        return ResponseEntity.ok(inProgress);
    }
}