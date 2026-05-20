package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import java.util.List;
import java.util.UUID;

public interface StudentProgressService {
  List<UserDailyMission> getStudentMissions(UUID userId);

  UserDailyMission updateProgress(UUID userId, Long missionId, Integer progress);

  Integer calculateTotalRewardPoints(UUID userId);
}