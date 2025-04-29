package tn.fst.spring.netvoyage.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsDTO {

    private long totalPublications;
    private long totalComments;
    private long totalLikes;
    private long totalDislikes;

    public StatisticsDTO(long totalPublications, long totalComments, long totalLikes, long totalDislikes) {
        this.totalPublications = totalPublications;
        this.totalComments = totalComments;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
    }

    // Getters and setters
}
