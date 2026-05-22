package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserAchievementRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAchievementServiceImpl implements UserAchievementService {
  private static final String QUIZ_COUNT = "QUIZ_COUNT";

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
    List<Achievement> allAchievements = achievementRepository.findAll();

    for (Achievement achievement : allAchievements) {
      if (isQuizCountAchievement(achievement) && quizCount >= achievement.getMilestone()) {
        unlockAchievementIfNeeded(userId, achievement);
      }
    }
  }

  @Override
  @Transactional
  public void checkAndUnlockAchievementsByType(UUID userId, String milestoneType) {
    if (milestoneType == null || milestoneType.isBlank()) {
      return;
    }

    achievementRepository.findAll().stream()
        .filter(achievement -> milestoneType.equals(achievement.getMilestoneType()))
        .filter(achievement -> achievement.getMilestone() <= 1)
        .forEach(achievement -> unlockAchievementIfNeeded(userId, achievement));
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

  private boolean isQuizCountAchievement(Achievement achievement) {
    String milestoneType = achievement.getMilestoneType();
    return milestoneType == null || milestoneType.isBlank() || QUIZ_COUNT.equals(milestoneType);
  }

  private void unlockAchievementIfNeeded(UUID userId, Achievement achievement) {
    boolean alreadyUnlocked = userAchievementRepository
        .existsByUserIdAndAchievementId(userId, achievement.getId());

    if (!alreadyUnlocked) {
      UserAchievement ua = new UserAchievement();
      ua.setUserId(userId);
      ua.setAchievement(achievement);
      ua.setUnlockedAt(LocalDateTime.now());
      ua.setShowcased(false);
      userAchievementRepository.save(ua);
    }
  }
}
