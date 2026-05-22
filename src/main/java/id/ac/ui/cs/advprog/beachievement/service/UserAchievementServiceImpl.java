package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserAchievementRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAchievementServiceImpl implements UserAchievementService {
  private final UserAchievementRepository userAchievementRepository;
  private final AchievementRepository achievementRepository;

  public UserAchievementServiceImpl(
      UserAchievementRepository userAchievementRepository,
      AchievementRepository achievementRepository) {
    this.userAchievementRepository = userAchievementRepository;
    this.achievementRepository = achievementRepository;
  }

  @Override
  public List<UserAchievement> getUnlockedAchievements(UUID userId) {
    return userAchievementRepository.findByUserId(userId);
  }

  @Override
  public List<UserAchievement> getPublicAchievements(UUID userId) {
    return userAchievementRepository.findByUserIdAndIsShowcasedTrue(userId);
  }

  @Override
  @Transactional
  public void checkAndUnlockAchievements(UUID userId, int quizCount) {
    List<Achievement> eligibleAchievements = achievementRepository
        .findEligibleQuizCountAchievements(quizCount);

    unlockMissingAchievements(userId, eligibleAchievements);
  }

  private void unlockMissingAchievements(UUID userId, List<Achievement> eligibleAchievements) {
    if (eligibleAchievements.isEmpty()) {
      return;
    }

    Set<Long> unlockedAchievementIds = new HashSet<>(
        userAchievementRepository.findAchievementIdsByUserId(userId));

    for (Achievement achievement : eligibleAchievements) {
      if (!unlockedAchievementIds.contains(achievement.getId())) {
        unlockAchievement(userId, achievement);
        unlockedAchievementIds.add(achievement.getId());
      }
    }
  }

  @Override
  @Transactional
  public void checkAndUnlockAchievementsByType(UUID userId, String milestoneType) {
    if (milestoneType == null || milestoneType.isBlank()) {
      return;
    }

    List<Achievement> eligibleAchievements = achievementRepository
        .findByMilestoneTypeAndMilestoneLessThanEqual(milestoneType, 1);
    unlockMissingAchievements(userId, eligibleAchievements);
  }

  @Override
  @Transactional
  public void setFeaturedAchievement(UUID userId, Long achievementId, boolean showcased) {
    userAchievementRepository.findByUserIdAndAchievementId(userId, achievementId)
        .ifPresent(ua -> {
          ua.setShowcased(showcased);
          userAchievementRepository.save(ua);
        });
  }

  private void unlockAchievement(UUID userId, Achievement achievement) {
    UserAchievement ua = new UserAchievement();
    ua.setUserId(userId);
    ua.setAchievement(achievement);
    ua.setUnlockedAt(LocalDateTime.now());
    ua.setShowcased(false);
    userAchievementRepository.save(ua);
  }
}
