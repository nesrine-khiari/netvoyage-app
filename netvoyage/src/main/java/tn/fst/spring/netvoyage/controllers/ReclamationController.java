package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.fst.spring.netvoyage.dtos.ReclamationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.fst.spring.netvoyage.dtos.ReclamationResponseDTO;
import tn.fst.spring.netvoyage.entities.ReclamationStatus;
import tn.fst.spring.netvoyage.entities.Voyageur;
import tn.fst.spring.netvoyage.services.interfaces.IReclamationService;
import tn.fst.spring.netvoyage.services.interfaces.IVoyageurService;
import tn.fst.spring.netvoyage.services.implementations.EntrepriseService;
import tn.fst.spring.netvoyage.utils.JWTUtils;
import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
@RequiredArgsConstructor
public class ReclamationController {
    private final IReclamationService service;
    private final JWTUtils jwtUtils;
    private final EntrepriseService entrepriseService;
    private final IVoyageurService voyageurService;





    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody ReclamationDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        // Création d'un logger
        Logger logger = LoggerFactory.getLogger(getClass());

        try {
            logger.info("Début de la création de réclamation");

            // Extraction de l'email depuis le token
            String token = authHeader.substring(7);
            String email = jwtUtils.getUsernameFromToken(token);
            logger.debug("Email extrait du token : {}", email);

            // Récupération du voyageur authentifié
            Optional<Voyageur> voyageurOpt = voyageurService.findByEmail(email);
            if (voyageurOpt.isEmpty()) {
                logger.warn("Utilisateur non trouvé pour l'email : {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Utilisateur non trouvé");
            }
            Voyageur authenticatedVoyageur = voyageurOpt.get();
            logger.debug("Utilisateur authentifié : {}", authenticatedVoyageur.getId());

            // Vérification que l'auteur de la réclamation est bien l'utilisateur authentifié
            if (!authenticatedVoyageur.getId().equals(dto.getAuthorId())) {
                logger.warn("Tentative de créer une réclamation pour un autre utilisateur. Utilisateur authentifié : {}, Auteur de la réclamation : {}", authenticatedVoyageur.getId(), dto.getAuthorId());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Vous ne pouvez pas créer une réclamation pour un autre utilisateur");
            }

            // Vérification que l'auteur et la cible appartiennent à la même entreprise
            Optional<Voyageur> targetOpt = voyageurService.getVoyageurById(dto.getTargetId());
            if (targetOpt.isEmpty()) {
                logger.warn("Cible non trouvée pour l'ID : {}", dto.getTargetId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Cible non trouvée");
            }
            Voyageur target = targetOpt.get();
            logger.debug("Cible trouvée : {}", target.getId());

            if (!authenticatedVoyageur.getEmploye().getEntreprise().getId()
                    .equals(target.getEmploye().getEntreprise().getId())) {
                logger.warn("Les utilisateurs ne font pas partie de la même entreprise. Auteur : {}, Cible : {}", authenticatedVoyageur.getEmploye().getEntreprise().getId(), target.getEmploye().getEntreprise().getId());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Vous ne pouvez pas créer une réclamation pour un employé d'une autre entreprise");
            }

            // Création de la réclamation
            logger.info("Réclamation créée avec succès pour l'auteur : {}", authenticatedVoyageur.getId());
            return ResponseEntity.ok(service.create(dto));

        } catch (Exception e) {
            logger.error("Erreur lors de la création de la réclamation", e);
            return ResponseEntity.internalServerError()
                    .body("Une erreur est survenue lors de la création de la réclamation: " + e.getMessage());
        }
    }

    @GetMapping("/entreprise/{entrepriseId}")
    public ResponseEntity<?> getByEntreprise(
            @PathVariable Long entrepriseId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
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
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String email = jwtUtils.getUsernameFromToken(token);
            Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email);

            ReclamationResponseDTO reclamation = service.getById(id);
            if (reclamation == null) {
                return ResponseEntity.notFound().build();
            }

            List<ReclamationResponseDTO> entrepriseReclamations = service.getByEntreprise(entrepriseIdFromToken);
            boolean hasAccess = entrepriseReclamations.stream()
                    .anyMatch(r -> r.getId().equals(id));

            if (!hasAccess) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Accès non autorisé à cette réclamation");
            }

            return ResponseEntity.ok(service.markAsResolved(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/entreprise/{entrepriseId}/status/{status}")
    @PreAuthorize("@securityService.isUserFromCompany(#entrepriseId)")
    public ResponseEntity<List<ReclamationResponseDTO>> getByStatusAndEntreprise(
            @PathVariable Long entrepriseId,
            @PathVariable ReclamationStatus status,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtils.getUsernameFromToken(token);
        Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email);

        if (!entrepriseId.equals(entrepriseIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

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
        return ResponseEntity.ok(service.countReclamationsByEntreprise(validEnt));
    }

    @GetMapping("/stats/entreprise/{entrepriseId}/resolved")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<Long> getResolvedByEntreprise(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long entrepriseId) {
        Long validEnt = validateAndGetEntrepriseId(entrepriseId, authHeader);
        return ResponseEntity.ok(service.countResolvedReclamationsByEntreprise(validEnt));
    }

    @GetMapping("/stats/entreprise/{entrepriseId}/in_progress")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<Long> getInProgressByEntreprise(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long entrepriseId) {
        Long validEnt = validateAndGetEntrepriseId(entrepriseId, authHeader);
        return ResponseEntity.ok(service.countInProgressReclamationsByEntreprise(validEnt));
    }

    private Long validateAndGetEntrepriseId(Long requestedEntrepriseId, String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Token manquant ou mal formaté");
        }
        String token = authHeader.substring(7);
        String email = jwtUtils.getUsernameFromToken(token);
        Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email);

        if (requestedEntrepriseId != null && !requestedEntrepriseId.equals(entrepriseIdFromToken)) {
            throw new SecurityException("Accès non autorisé à cette entreprise");
        }
        return entrepriseIdFromToken;
    }
}