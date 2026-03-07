package id.ac.ui.cs.advprog.beachievement.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyMissionRequest {
  private String title;
  private String description;
  private Integer targetMilestone;
  private Integer rewardPoints;
}