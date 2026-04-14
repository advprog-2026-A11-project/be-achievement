package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchievementListenerServiceImpl implements AchievementListenerService {

  private final DailyMissionRepository dailyMissionRepository;
  private final UserDailyMissionRepository userDailyMissionRepository;

  public AchievementListenerServiceImpl(
      DailyMissionRepository dailyMissionRepository,
      UserDailyMissionRepository userDailyMissionRepository) {
    this.dailyMissionRepository = dailyMissionRepository;
    this.userDailyMissionRepository = userDailyMissionRepository;
  }

  @Override
  @Transactional
  public void processQuizCompleted(QuizCompletedEvent event) {
    List<DailyMission> todayMissions = dailyMissionRepository
        .findByActiveDate(LocalDate.now());

    for (DailyMission mission : todayMissions) {
      UserDailyMission udm = userDailyMissionRepository
          .findByUserIdAndDailyMissionId(event.getUserId(), mission.getId())
          .orElseGet(() -> {
            UserDailyMission newUdm = new UserDailyMission();
            newUdm.setUserId(event.getUserId());
            newUdm.setDailyMission(mission);
            newUdm.setCurrentProgress(0);
            newUdm.setCompleted(false);
            return newUdm;
          });
      if (!udm.isCompleted()) {
        udm.setCurrentProgress(udm.getCurrentProgress() + 1);
        if (udm.getCurrentProgress() >= mission.getTargetMilestone()) {
          udm.setCompleted(true);
        }
        userDailyMissionRepository.save(udm);
      }
    }
  }
}