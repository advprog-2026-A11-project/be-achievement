package id.ac.ui.cs.advprog.beachievement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyMissionRequest {
  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Description is required")
  private String description;

  @NotNull(message = "Target milestone is required")
  @Min(value = 1, message = "Target milestone must be at least 1")
  private Integer targetMilestone;

  private Integer rewardPoints;

  private String missionType;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate activeDate;
}
