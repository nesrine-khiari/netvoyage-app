package tn.fst.spring.netvoyage.dtos;

import lombok.Data;

@Data
public class RegisterEntrepriseRequest {
    private String email;
    private String password;
    private String username;
    private String nomEntreprise;
    private String secteur;
    private String pays;
}
