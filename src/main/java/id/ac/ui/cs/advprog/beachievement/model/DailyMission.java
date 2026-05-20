package id.ac.ui.cs.advprog.beachievement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "daily_mission")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class DailyMission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(name = "target_milestone", nullable = false)
  private Integer targetMilestone;

  private Integer rewardPoints;

  private String missionType;

  private Boolean isActive = true;

  public boolean isActive() {
    return this.isActive != null && this.isActive;
  }

  public void setActive(Boolean active) {
    this.isActive = active;
  }

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate activeDate;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}