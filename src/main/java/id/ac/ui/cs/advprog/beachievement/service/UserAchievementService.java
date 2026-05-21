package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import java.util.List;
import java.util.UUID;

public interface UserAchievementService {
  List<UserAchievement> getUnlockedAchievements(UUID userId);

  List<UserAchievement> getPublicAchievements(UUID userId);

  void checkAndUnlockAchievements(UUID userId, int quizCount);

  void checkAndUnlockAchievementsByType(UUID userId, String milestoneType);

  void setFeaturedAchievement(UUID userId, Long achievementId, boolean showcased);
}
