package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public List<UserDailyMission> getStudentMissions(UUID userId) {
    List<DailyMission> todayMissions = dailyMissionRepository
        .findByActiveDate(LocalDate.now());

    for (DailyMission mission : todayMissions) {
      getOrCreateUserDailyMission(userId, mission);
    }

    return userDailyMissionRepository.findByUserIdAndDailyMissionActiveDate(userId, LocalDate.now());
  }

  @Override
  public UserDailyMission updateProgress(UUID userId, Long missionId, Integer progress) {
    UserDailyMission udm = findUserDailyMission(userId, missionId)
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

  private java.util.Optional<UserDailyMission> findUserDailyMission(UUID userId, Long missionId) {
    return userDailyMissionRepository.findAllByUserIdAndDailyMissionIdOrderByIdAsc(userId,
            missionId)
        .stream()
        .findFirst();
  }

  private UserDailyMission getOrCreateUserDailyMission(UUID userId, DailyMission mission) {
    return findUserDailyMission(userId, mission.getId())
        .orElseGet(() -> createUserDailyMission(userId, mission));
  }

  private UserDailyMission createUserDailyMission(UUID userId, DailyMission mission) {
    UserDailyMission udm = new UserDailyMission();
    udm.setUserId(userId);
    udm.setDailyMission(mission);
    udm.setCurrentProgress(0);
    udm.setCompleted(false);

    try {
      return userDailyMissionRepository.saveAndFlush(udm);
    } catch (DataIntegrityViolationException e) {
      return findUserDailyMission(userId, mission.getId())
          .orElseThrow(() -> e);
    }
  }
}
