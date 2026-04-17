package id.ac.ui.cs.advprog.beachievement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_achievements", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "achievement_id" }))
@Getter
@Setter
public class UserAchievement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @ManyToOne
  @JoinColumn(name = "achievement_id", nullable = false)
  private Achievement achievement;

  @Column(name = "unlocked_at")
  private LocalDateTime unlockedAt;

  @Column(name = "is_showcased")
  private boolean isShowcased = false;
}