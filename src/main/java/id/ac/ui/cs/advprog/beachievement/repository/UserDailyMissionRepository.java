package id.ac.ui.cs.advprog.beachievement.repository;

import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDailyMissionRepository extends JpaRepository<UserDailyMission, Long> {
  List<UserDailyMission> findByUserId(UUID userId);

  Optional<UserDailyMission> findByUserIdAndDailyMissionId(UUID userId, Long missionId);
}