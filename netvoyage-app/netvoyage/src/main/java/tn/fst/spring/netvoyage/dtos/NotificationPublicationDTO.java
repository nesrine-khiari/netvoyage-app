package tn.fst.spring.netvoyage.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotificationPublicationDTO {
    private String title;
    private String content;
    private Long numPublication;
}
