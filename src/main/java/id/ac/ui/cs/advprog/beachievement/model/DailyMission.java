package id.ac.ui.cs.advprog.beachievement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "daily_mission")
@Getter
@Setter
public class DailyMission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private Integer targetMilestone;
  private Integer rewardPoints;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate activeDate;
}