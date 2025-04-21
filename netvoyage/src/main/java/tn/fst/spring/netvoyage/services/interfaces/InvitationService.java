package tn.fst.spring.netvoyage.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import tn.fst.spring.netvoyage.entities.Invitation;
import tn.fst.spring.netvoyage.enums.InvitationStatus;

import java.util.List;

public interface InvitationService {
    Invitation createInvitation(String nom, String email, Long entrepriseId);
    Invitation updateInvitation(Long id, Invitation updated);
    void deleteInvitation(Long id);
    Invitation getInvitationById(Long id);
    List<Invitation> getAllInvitations();
    void sendBatchInvitations(MultipartFile file, Long entrepriseId);
    Invitation acceptInvitation(String token);
    List<Invitation> getInvitationsByEntreprise(Long entrepriseId);
    List<Invitation> getInvitationsByEntrepriseAndStatus(Long entrepriseId, InvitationStatus status);
}

