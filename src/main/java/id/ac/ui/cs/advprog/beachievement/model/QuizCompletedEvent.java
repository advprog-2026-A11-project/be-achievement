package id.ac.ui.cs.advprog.beachievement.model;

import java.util.UUID;
import lombok.Data;

@Data
public class QuizCompletedEvent {
  private UUID userId;
  private Integer score;
  private Double accuracy;
}
