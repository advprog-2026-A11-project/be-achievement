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
      if (quizCount >= achievement.getMilestone()) {
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
}