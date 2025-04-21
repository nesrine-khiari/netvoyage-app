package tn.fst.spring.netvoyage.dtos;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntrepriseDTO {
    private String nomEntreprise;
    private String pays;
    private String adresse;
    private String telephone;
    private Long domaineId;
}
