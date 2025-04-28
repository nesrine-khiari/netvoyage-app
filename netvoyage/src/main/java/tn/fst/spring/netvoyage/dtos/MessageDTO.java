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


    public MessageDTO(String content, Long senderId, String senderName) {
        this.content = content;
        this.senderId = senderId;
        this.senderName = senderName;
    }

    public MessageDTO() {
    }
}
