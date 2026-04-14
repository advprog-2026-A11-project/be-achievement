package id.ac.ui.cs.advprog.beachievement.model;

import lombok.Data;
import java.util.UUID;

@Data
public class QuizCompletedEvent {
    private UUID userId;
    private Integer score;
    private Double accuracy;
}
