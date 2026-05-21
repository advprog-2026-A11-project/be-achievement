package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
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
