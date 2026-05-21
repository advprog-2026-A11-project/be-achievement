package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentProgressServiceImpl implements StudentProgressService {

  private final UserDailyMissionRepository userDailyMissionRepository;
  private final DailyMissionRepository dailyMissionRepository;
  private final UserDailyMissionProgressService userDailyMissionProgressService;

  public StudentProgressServiceImpl(
      UserDailyMissionRepository userDailyMissionRepository,
      DailyMissionRepository dailyMissionRepository,
      UserDailyMissionProgressService userDailyMissionProgressService) {
    this.userDailyMissionRepository = userDailyMissionRepository;
    this.dailyMissionRepository = dailyMissionRepository;
    this.userDailyMissionProgressService = userDailyMissionProgressService;
  }

  @Override
  @Transactional
  public List<UserDailyMission> getStudentMissions(UUID userId) {
    List<DailyMission> todayMissions = dailyMissionRepository
        .findByActiveDate(LocalDate.now());

    for (DailyMission mission : todayMissions) {
      userDailyMissionProgressService.getOrCreateUserDailyMission(userId, mission);
    }

    return userDailyMissionRepository.findByUserIdAndDailyMissionActiveDate(
        userId, LocalDate.now());
  }

  @Override
  public UserDailyMission updateProgress(UUID userId, Long missionId, Integer progress) {
    UserDailyMission udm = userDailyMissionProgressService.findUserDailyMission(userId, missionId)
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
