package tn.fst.spring.netvoyage.dtos;

import lombok.Data;

@Data
public class EmployeDTO {
    private String firstname;
    private String lastname;
    private String adresse;
    private String telephone;
    private Long entrepriseId;
}
