package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class StudentProgressServiceImpl implements StudentProgressService {

  private final UserDailyMissionRepository userDailyMissionRepository;
  private final DailyMissionRepository dailyMissionRepository;

  public StudentProgressServiceImpl(
      UserDailyMissionRepository userDailyMissionRepository,
      DailyMissionRepository dailyMissionRepository) {
    this.userDailyMissionRepository = userDailyMissionRepository;
    this.dailyMissionRepository = dailyMissionRepository;
  }

  @Override
  public List<UserDailyMission> getStudentMissions(UUID userId) {
    List<DailyMission> todayMissions = dailyMissionRepository
        .findByActiveDate(LocalDate.now());

    for (DailyMission mission : todayMissions) {
      userDailyMissionRepository
          .findByUserIdAndDailyMissionId(userId, mission.getId())
          .orElseGet(() -> {
            UserDailyMission udm = new UserDailyMission();
            udm.setUserId(userId);
            udm.setDailyMission(mission);
            udm.setCurrentProgress(0);
            udm.setCompleted(false);
            return userDailyMissionRepository.save(udm);
          });
    }

    return userDailyMissionRepository.findByUserId(userId);
  }

  @Override
  public UserDailyMission updateProgress(UUID userId, Long missionId, Integer progress) {
    UserDailyMission udm = userDailyMissionRepository
        .findByUserIdAndDailyMissionId(userId, missionId)
        .orElseThrow(() -> new RuntimeException("Mission not found for user"));

    udm.setCurrentProgress(progress);
    if (progress >= udm.getDailyMission().getTargetMilestone()) {
      udm.setCompleted(true);
    }
    return userDailyMissionRepository.save(udm);
  }

  @Override
  public Integer calculateTotalRewardPoints(UUID userId) {
    return userDailyMissionRepository.calculateTotalRewardPoints(userId);
  }
}