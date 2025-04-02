package tn.fst.spring.netvoyage.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private Long numMessage;
    private String content;
    private Long senderId;  // Added senderId field
    private String senderName; // Optional: to show sender's name
    private Long discussionId;
}
