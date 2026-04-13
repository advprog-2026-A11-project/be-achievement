package id.ac.ui.cs.advprog.beachievement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyMissionRequest {
  private String title;
  private String description;
  private Integer targetMilestone;
  private Integer rewardPoints;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate activeDate;
}