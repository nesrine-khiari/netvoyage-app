package tn.fst.spring.netvoyage.dtos;

import lombok.Data;

@Data
public class InvitationDTO {
    private String nom;
    private String email;
    private Long entrepriseId;
}
