package tn.fst.spring.netvoyage.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicationDTO {
    private Long numPublication;
    private Long numOwner;
    private String title;
    private String content;
}
