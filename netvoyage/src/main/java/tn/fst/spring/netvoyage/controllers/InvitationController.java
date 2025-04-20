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

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    private final JWTUtils jwtUtils; // Injection de JWTUtils
    private final EntrepriseService entrepriseService; // Injection du service EntrepriseService

    // Batch upload (uniquement avec un token valide pour l'entreprise spécifique)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<String> uploadInvitations(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long entrepriseId,
            @RequestHeader("Authorization") String authHeader) {  // Récupérer l'en-tête Authorization pour le token JWT
        try {
            // Extraire le token JWT de l'en-tête
            String token = authHeader.substring(7);  // Enlever le préfixe "Bearer " du token

            String email = jwtUtils.getUsernameFromToken(token);  // "Username" ici est en fait l'email
            System.out.println("Email extrait du token: " + email);


            // Récupérer l'ID de l'entreprise à partir du username
            Long entrepriseIdFromToken = entrepriseService.getEntrepriseIdByEmail(email);

            // Vérifier si l'ID de l'entreprise dans le token correspond à celui passé dans la requête
            if (!entrepriseId.equals(entrepriseIdFromToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Vous n'êtes pas autorisé à télécharger des invitations pour cette entreprise.");
            }

            // Appeler le service pour traiter le fichier
            invitationService.sendBatchInvitations(file, entrepriseId);
            return ResponseEntity.ok("Fichier traité avec succès - les emails sont en cours d'envoi");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur : " + e.getMessage());
        }
    }

    // Create a single invitation
    @PostMapping
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<Invitation> createInvitation(
            @Valid @RequestBody InvitationRequest request) {
        try {
            Invitation invitation = invitationService.createInvitation(
                    request.getNom(), request.getEmail(), request.getEntrepriseId());
            return ResponseEntity.status(HttpStatus.CREATED).body(invitation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    // Update an invitation
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<Invitation> updateInvitation(
            @PathVariable Long id,
            @Valid @RequestBody InvitationRequest request) {
        try {
            Invitation updated = new Invitation();
            updated.setNomInvite(request.getNom());
            updated.setEmailInvite(request.getEmail());
            // Status can be updated separately if needed
            Invitation invitation = invitationService.updateInvitation(id, updated);
            return ResponseEntity.ok(invitation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
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
    public ResponseEntity<Invitation> getInvitationById(@PathVariable Long id) {
        try {
            Invitation invitation = invitationService.getInvitationById(id);
            return ResponseEntity.ok(invitation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // Get all invitations
    @GetMapping
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<List<Invitation>> getAllInvitations() {
        return ResponseEntity.ok(invitationService.getAllInvitations());
    }

    // Get invitations by entreprise
    @GetMapping("/entreprise/{entrepriseId}")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<List<Invitation>> getInvitationsByEntreprise(@PathVariable Long entrepriseId) {
        return ResponseEntity.ok(invitationService.getInvitationsByEntreprise(entrepriseId));
    }

    // Get invitations by entreprise and status
    @GetMapping("/entreprise/{entrepriseId}/status")
    @PreAuthorize("hasRole('ENTREPRISE')")
    public ResponseEntity<List<Invitation>> getInvitationsByStatus(
            @PathVariable Long entrepriseId,
            @RequestParam InvitationStatus status) {
        return ResponseEntity.ok(invitationService.getInvitationsByEntrepriseAndStatus(entrepriseId, status));
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

}