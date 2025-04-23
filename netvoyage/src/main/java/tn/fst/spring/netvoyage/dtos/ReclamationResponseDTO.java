package tn.fst.spring.netvoyage.dtos;

import lombok.Builder;
import lombok.Data;
import tn.fst.spring.netvoyage.entities.ReclamationStatus;

@Data
@Builder
public class ReclamationResponseDTO {
    private Long id;
    private String titre;
    private String description;
    private ReclamationStatus status;
    private Long authorId;
    private String authorName;
    private Long targetId;
    private String targetName;
    private Long voyageId;
}