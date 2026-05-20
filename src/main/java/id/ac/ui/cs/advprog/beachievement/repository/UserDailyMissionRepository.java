package id.ac.ui.cs.advprog.beachievement.repository;

import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDailyMissionRepository extends JpaRepository<UserDailyMission, Long> {
  List<UserDailyMission> findByUserId(UUID userId);

  Optional<UserDailyMission> findByUserIdAndDailyMissionId(UUID userId, Long missionId);

  @Query("SELECT COALESCE(SUM(dm.rewardPoints), 0) FROM UserDailyMission udm "
      + "JOIN udm.dailyMission dm WHERE udm.userId = :userId AND udm.isCompleted = true")
  Integer calculateTotalRewardPoints(@Param("userId") UUID userId);
}