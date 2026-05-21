package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserQuizCount;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserQuizCountRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchievementListenerServiceImpl implements AchievementListenerService {
  private static final String QUIZ_ACCURACY = "QUIZ_ACCURACY";

  private final DailyMissionRepository dailyMissionRepository;
  private final UserDailyMissionRepository userDailyMissionRepository;
  private final UserQuizCountRepository userQuizCountRepository;
  private final UserAchievementService userAchievementService;
  private final UserDailyMissionProgressService userDailyMissionProgressService;

  public AchievementListenerServiceImpl(
      DailyMissionRepository dailyMissionRepository,
      UserDailyMissionRepository userDailyMissionRepository,
      UserQuizCountRepository userQuizCountRepository,
      UserAchievementService userAchievementService,
      UserDailyMissionProgressService userDailyMissionProgressService) {
    this.dailyMissionRepository = dailyMissionRepository;
    this.userDailyMissionRepository = userDailyMissionRepository;
    this.userQuizCountRepository = userQuizCountRepository;
    this.userAchievementService = userAchievementService;
    this.userDailyMissionProgressService = userDailyMissionProgressService;
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

  private boolean matchesMissionRule(DailyMission mission, QuizCompletedEvent event) {
    String missionType = mission.getMissionType();
    if (missionType == null || missionType.isBlank() || "QUIZ_COUNT".equals(missionType)) {
      return true;
    }
    return QUIZ_ACCURACY.equals(missionType) && isPerfectAccuracyEvent(event);
  }

  private boolean isPerfectAccuracyEvent(QuizCompletedEvent event) {
    return event.getAccuracy() != null && event.getAccuracy() >= 100.0;
  }
}
