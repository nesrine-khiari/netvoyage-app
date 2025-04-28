package tn.fst.spring.netvoyage.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReclamationDTO {
    private String titre;
    private String description;
    private Long authorId;
    private Long targetId;
    private Long voyageId;
}