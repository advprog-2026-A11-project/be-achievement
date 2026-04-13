package id.ac.ui.cs.advprog.beachievement.model;

import lombok.Data;

@Data
public class QuizCompletedEvent {
    private String userId;
    private Integer score;
    private Double accuracy;
}
