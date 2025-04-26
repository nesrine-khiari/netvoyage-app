package tn.fst.spring.netvoyage.dtos;

import lombok.Data;

import java.time.LocalDate;

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
}

