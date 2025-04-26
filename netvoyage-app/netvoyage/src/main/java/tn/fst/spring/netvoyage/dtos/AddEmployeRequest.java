package tn.fst.spring.netvoyage.dtos;

import lombok.Data;

@Data
public class AddEmployeRequest {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private Long entrepriseId;
}
