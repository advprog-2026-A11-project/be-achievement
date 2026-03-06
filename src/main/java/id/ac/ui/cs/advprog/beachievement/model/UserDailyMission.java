package id.ac.ui.cs.advprog.beachievement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_daily_mission")
@Getter
@Setter
public class UserDailyMission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userId;

  @ManyToOne
  @JoinColumn(name = "mission_id")
  private DailyMission dailyMission;

  private Integer currentProgress = 0;
  private boolean isCompleted = false;
}