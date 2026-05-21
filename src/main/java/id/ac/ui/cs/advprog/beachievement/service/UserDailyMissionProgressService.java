package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserDailyMissionProgressService {

  private final UserDailyMissionRepository userDailyMissionRepository;

  public UserDailyMissionProgressService(UserDailyMissionRepository userDailyMissionRepository) {
    this.userDailyMissionRepository = userDailyMissionRepository;
  }

  public Optional<UserDailyMission> findUserDailyMission(UUID userId, Long missionId) {
    return userDailyMissionRepository.findAllByUserIdAndDailyMissionIdOrderByIdAsc(userId,
            missionId)
        .stream()
        .findFirst();
  }

  public UserDailyMission getOrCreateUserDailyMission(UUID userId, DailyMission mission) {
    return findUserDailyMission(userId, mission.getId())
        .orElseGet(() -> insertAndReloadUserDailyMission(userId, mission));
  }

  private UserDailyMission insertAndReloadUserDailyMission(UUID userId, DailyMission mission) {
    userDailyMissionRepository.insertIfMissing(userId, mission.getId());
    return findUserDailyMission(userId, mission.getId())
        .orElseThrow(() -> new IllegalStateException("Failed to create user daily mission"));
  }
}
