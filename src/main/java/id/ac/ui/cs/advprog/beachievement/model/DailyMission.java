package id.ac.ui.cs.advprog.beachievement.model;

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

  private LocalDate activeDate;
}