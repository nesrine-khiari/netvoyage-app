package tn.fst.spring.netvoyage.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessionStatsDTO {
    private String nom;
    private int nombreEmployes;
    private double pourcentage;

    // Constructeur
    public ProfessionStatsDTO(String nom, int nombreEmployes, double pourcentage) {
        this.nom = nom;
        this.nombreEmployes = nombreEmployes;
        this.pourcentage = pourcentage;
    }


}
