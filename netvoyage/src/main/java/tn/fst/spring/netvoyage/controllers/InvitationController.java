package tn.fst.spring.netvoyage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.fst.spring.netvoyage.dtos.InvitationRequest;
import tn.fst.spring.netvoyage.entities.Invitation;
import tn.fst.spring.netvoyage.enums.InvitationStatus;
import tn.fst.spring.netvoyage.services.interfaces.InvitationService;
import tn.fst.spring.netvoyage.utils.JWTUtils;  // Import de JWTUtils
import tn.fst.spring.netvoyage.services.implementations.EntrepriseService;  // Import de EntrepriseService

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.JwtException;


@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private static final Logger log = LoggerFactory.getLogger(InvitationController.class);

    private final InvitationService invitationService;
    private final JWTUtils jwtUtils; // Injection de JWTUtils
    private final EntrepriseService entrepriseService; // Injection du service EntrepriseService

    private Long validateAndGetEntrepriseId(Long requestedEntrepriseId, String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtils.getUsernameFromToken(token);
        Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email);

        if (requestedEntrepriseId != null && !requestedEntrepriseId.equals(entrepriseIdFromToken)) {
            throw new SecurityException("Accès non autorisé à cette entreprise");
        }
        return entrepriseIdFromToken;
    }


    // Batch upload
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<String> uploadInvitations(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long entrepriseId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            validateAndGetEntrepriseId(entrepriseId, authHeader);
            invitationService.sendBatchInvitations(file, entrepriseId);
            return ResponseEntity.ok("Fichier traité avec succès");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur: " + e.getMessage());
        }
    }

    // Create a single invitation
    @PostMapping
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<?> createInvitation(
            @Valid @RequestBody InvitationRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            Long entrepriseId = validateAndGetEntrepriseId(request.getEntrepriseId(), authHeader);
            Invitation invitation = invitationService.createInvitation(
                    request.getNom(),
                    request.getEmail(),
                    entrepriseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(invitation);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }

    // Update an invitation
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<?> updateInvitation(
            @PathVariable Long id,
            @Valid @RequestBody InvitationRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Vérifier que l'invitation appartient bien à l'entreprise
            Invitation existing = invitationService.getInvitationById(id);
            validateAndGetEntrepriseId(existing.getEntreprise().getId(), authHeader);

            Invitation updated = new Invitation();
            updated.setNomInvite(request.getNom());
            updated.setEmailInvite(request.getEmail());
            Invitation result = invitationService.updateInvitation(id, updated);
            return ResponseEntity.ok(result);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an invitation
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<String> deleteInvitation(@PathVariable Long id) {
        try {
            invitationService.deleteInvitation(id);
            return ResponseEntity.ok("Invitation supprimée avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invitation non trouvée");
        }
    }

    // Get an invitation by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<?> getInvitationById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            Invitation invitation = invitationService.getInvitationById(id);
            validateAndGetEntrepriseId(invitation.getEntreprise().getId(), authHeader);
            return ResponseEntity.ok(invitation);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all invitations
    @GetMapping
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<?> getAllInvitations(
            @RequestHeader("Authorization") String authHeader) {
        try {
            Long entrepriseId = validateAndGetEntrepriseId(null, authHeader);
            return ResponseEntity.ok(invitationService.getInvitationsByEntreprise(entrepriseId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur serveur");
        }
    }

    // Get invitations by entreprise
    @GetMapping("/entreprise/{entrepriseId}")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<?> getInvitationsByEntreprise(
            @PathVariable Long entrepriseId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            validateAndGetEntrepriseId(entrepriseId, authHeader);
            return ResponseEntity.ok(invitationService.getInvitationsByEntreprise(entrepriseId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur serveur");
        }
    }

    @GetMapping("/entreprise/{entrepriseId}/status/{status}")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<?> getInvitationsByStatus(
            @PathVariable Long entrepriseId,
            @PathVariable InvitationStatus status,  // Utilisation de @PathVariable pour le statut
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // Log d'entrée pour débogage
        log.debug("Tentative d'accès aux invitations. Entreprise: {}, Status: {}", entrepriseId, status);

        try {
            // Vérification du header Authorization
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Token JWT manquant ou mal formaté");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Authentification requise");
            }

            // Validation de l'entreprise
            Long validatedEntrepriseId = validateAndGetEntrepriseId(entrepriseId, authHeader);
            log.debug("Validation réussie pour l'entreprise: {}", validatedEntrepriseId);

            // Récupération des invitations
            List<Invitation> invitations = invitationService
                    .getInvitationsByEntrepriseAndStatus(validatedEntrepriseId, status);

            log.info("Retour de {} invitations pour l'entreprise {}", invitations.size(), validatedEntrepriseId);
            return ResponseEntity.ok(invitations);

        } catch (SecurityException e) {
            log.error("Accès refusé: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur serveur", e);
            return ResponseEntity.internalServerError()
                    .body("Erreur lors du traitement de la requête");
        }
    }


    @GetMapping("/accept")
    public ResponseEntity<String> acceptInvitation(@RequestParam String token) {
        try {
            invitationService.acceptInvitation(token);
            return ResponseEntity.ok("Invitation acceptée avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/entreprise/{entrepriseId}/statistics")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<Map<String, Long>> getInvitationStatistics(@PathVariable Long entrepriseId) {
        Map<String, Long> stats = invitationService.getInvitationStatisticsByEntreprise(entrepriseId);
        return ResponseEntity.ok(stats);
    }


}