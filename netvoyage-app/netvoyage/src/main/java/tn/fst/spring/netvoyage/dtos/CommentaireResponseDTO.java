package tn.fst.spring.netvoyage.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentaireResponseDTO {
    private Long numCommentaire;
    private Long numOwner;
    private Long numPublication;
    private String content;
}
