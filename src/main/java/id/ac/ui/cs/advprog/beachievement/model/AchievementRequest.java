package id.ac.ui.cs.advprog.beachievement.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AchievementRequest {
  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Description is required")
  private String description;

  @NotNull(message = "Milestone is required")
  @Min(value = 1, message = "Milestone must be at least 1")
  private Integer milestone;
}