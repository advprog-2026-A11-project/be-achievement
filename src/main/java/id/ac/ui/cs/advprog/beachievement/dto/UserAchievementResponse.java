package id.ac.ui.cs.advprog.beachievement.dto;

import java.time.LocalDateTime;

import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAchievementResponse {
  private Long id;
  private Long achievementId;
  private String title;
  private String description;
  private Integer milestone;
  private String milestoneType;
  private String iconUrl;
  private LocalDateTime unlockedAt;
  private boolean isShowcased;

  public static UserAchievementResponse fromEntity(UserAchievement entity) {
    return UserAchievementResponse.builder()
        .id(entity.getId())
        .achievementId(entity.getAchievement().getId())
        .title(entity.getAchievement().getTitle())
        .description(entity.getAchievement().getDescription())
        .milestone(entity.getAchievement().getMilestone())
        .milestoneType(entity.getAchievement().getMilestoneType())
        .iconUrl(entity.getAchievement().getIconUrl())
        .unlockedAt(entity.getUnlockedAt())
        .isShowcased(entity.isShowcased())
        .build();
  }
}
