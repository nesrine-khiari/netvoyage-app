package tn.fst.spring.netvoyage.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VoyageDTO {
    private Long id;
    private String nom;
    private String destination;
    private LocalDate dateDepart;
    private LocalDate dateRetour;
    private String objetVoyage;
    private String perimetre;
    private Long organisateurId;
    private String organisateurNom;
    private Long entrepriseId;
    private List<Long> participantsIds;  // Liste des IDs des participants
    private List<String> participantsNoms;  // Liste des noms des participants
}
