package id.ac.ui.cs.advprog.beachievement.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.ClanPromotedEvent;
import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserQuizCount;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserAchievementRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserQuizCountRepository;

@Service
public class AchievementListenerServiceImpl implements AchievementListenerService {
  private static final String QUIZ_COUNT = "QUIZ_COUNT";
  private static final String QUIZ_ACCURACY = "QUIZ_ACCURACY";
  private static final String CLAN_DIAMOND = "CLAN_DIAMOND";
  private static final String READ_PREFIX = "READ_";

  private final DailyMissionRepository dailyMissionRepository;
  private final UserDailyMissionRepository userDailyMissionRepository;
  private final UserQuizCountRepository userQuizCountRepository;
  private final UserAchievementService userAchievementService;
  private final UserDailyMissionProgressService userDailyMissionProgressService;
  private final AchievementRepository achievementRepository;
  private final UserAchievementRepository userAchievementRepository;

  public AchievementListenerServiceImpl(
      DailyMissionRepository dailyMissionRepository,
      UserDailyMissionRepository userDailyMissionRepository,
      UserQuizCountRepository userQuizCountRepository,
      UserAchievementService userAchievementService,
      UserDailyMissionProgressService userDailyMissionProgressService,
      AchievementRepository achievementRepository,
      UserAchievementRepository userAchievementRepository) {
    this.dailyMissionRepository = dailyMissionRepository;
    this.userDailyMissionRepository = userDailyMissionRepository;
    this.userQuizCountRepository = userQuizCountRepository;
    this.userAchievementService = userAchievementService;
    this.userDailyMissionProgressService = userDailyMissionProgressService;
    this.achievementRepository = achievementRepository;
    this.userAchievementRepository = userAchievementRepository;
  }

  @Override
  @Transactional
  public void processQuizCompleted(QuizCompletedEvent event) {
    UUID userId = event.getUserId();
    String eventId = event.getEventId();

    UserQuizCount quizCount = userQuizCountRepository.findByUserId(userId)
        .orElseGet(() -> {
          UserQuizCount newCount = new UserQuizCount();
          newCount.setUserId(userId);
          newCount.setQuizCount(0);
          return newCount;
        });

    if (eventId != null && eventId.equals(quizCount.getLastProcessedEventId())) {
      return;
    }

    quizCount.setQuizCount(quizCount.getQuizCount() + 1);
    quizCount.setLastProcessedEventId(eventId);
    userQuizCountRepository.save(quizCount);
    userAchievementService.checkAndUnlockAchievements(userId, quizCount.getQuizCount());
    if (isPerfectAccuracyEvent(event)) {
      userAchievementService.checkAndUnlockAchievementsByType(userId, QUIZ_ACCURACY);
    }

    List<DailyMission> todayMissions = dailyMissionRepository
        .findByActiveDate(LocalDate.now());

    for (DailyMission mission : todayMissions) {
      UserDailyMission udm = userDailyMissionProgressService.getOrCreateUserDailyMission(userId,
          mission);

      if (!udm.isCompleted() && matchesMissionRule(mission, event)) {
        udm.setCurrentProgress(udm.getCurrentProgress() + 1);
        if (udm.getCurrentProgress() >= mission.getTargetMilestone()) {
          udm.setCompleted(true);
        }
        userDailyMissionRepository.save(udm);
      }
    }
  }

  @Override
  @Transactional
  public void processClanPromoted(ClanPromotedEvent event) {
    List<UUID> userIds = event.getUserIds();
    if (userIds == null || userIds.stream().noneMatch(id -> id != null)) {
      return;
    }

    Achievement diamondAchievement = achievementRepository.findAll().stream()
        .filter(a -> CLAN_DIAMOND.equals(a.getMilestoneType())
            || "Diamond".equalsIgnoreCase(a.getTitle()))
        .findFirst()
        .orElseGet(() -> {
          Achievement newAchievement = new Achievement();
          newAchievement.setTitle("Diamond");
          newAchievement.setDescription("Succeed in promoting clan to Diamond tier");
          newAchievement.setMilestone(1);
          newAchievement.setMilestoneType(CLAN_DIAMOND);
          newAchievement.setIconUrl("http://example.com/diamond.png");
          return achievementRepository.save(newAchievement);
        });

    for (UUID userId : userIds) {
      if (userId == null) {
        continue;
      }
      boolean alreadyUnlocked = userAchievementRepository
          .existsByUserIdAndAchievementId(userId, diamondAchievement.getId());

      if (!alreadyUnlocked) {
        UserAchievement ua = new UserAchievement();
        ua.setUserId(userId);
        ua.setAchievement(diamondAchievement);
        ua.setUnlockedAt(LocalDateTime.now());
        ua.setShowcased(false);
        userAchievementRepository.save(ua);
      }
    }
  }

  private boolean matchesMissionRule(DailyMission mission, QuizCompletedEvent event) {
    String missionType = mission.getMissionType();
    if (missionType == null || missionType.isBlank() || QUIZ_COUNT.equals(missionType)) {
      return true;
    }
    if (QUIZ_ACCURACY.equals(missionType)) {
      return isPerfectAccuracyEvent(event);
    }
    if (missionType.startsWith(READ_PREFIX)) {
      return matchesReadingCategory(missionType, event.getCategory());
    }
    return false;
  }

  private boolean isPerfectAccuracyEvent(QuizCompletedEvent event) {
    if (event.getAccuracy() == null) {
      return false;
    }

    double accuracy = event.getAccuracy();

    return accuracy == 1.0 || accuracy == 100.0;
  }

  private boolean matchesReadingCategory(String missionType, String category) {
    if (category == null || category.isBlank()) {
      return false;
    }

    String expectedCategory = normalizeRuleText(missionType.substring(READ_PREFIX.length()));
    String actualCategory = normalizeRuleText(category);

    if ("FICTION".equals(expectedCategory)) {
      return actualCategory.contains("FICTION")
          || actualCategory.contains("FIKSI")
          || actualCategory.contains("SASTRA");
    }

    return actualCategory.equals(expectedCategory)
        || actualCategory.startsWith(expectedCategory + "_")
        || actualCategory.contains("_" + expectedCategory + "_")
        || actualCategory.endsWith("_" + expectedCategory);
  }

  private String normalizeRuleText(String value) {
    return value.trim()
        .toUpperCase()
        .replaceAll("[^A-Z0-9]+", "_")
        .replaceAll("^_+|_+$", "");
  }
}
