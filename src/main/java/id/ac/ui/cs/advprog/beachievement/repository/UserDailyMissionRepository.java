package id.ac.ui.cs.advprog.beachievement.repository;

import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDailyMissionRepository extends JpaRepository<UserDailyMission, Long> {
  List<UserDailyMission> findByUserId(UUID userId);

  @Query("SELECT udm FROM UserDailyMission udm JOIN udm.dailyMission dm "
      + "WHERE udm.userId = :userId AND dm.activeDate = :activeDate")
  List<UserDailyMission> findByUserIdAndDailyMissionActiveDate(
      @Param("userId") UUID userId,
      @Param("activeDate") LocalDate activeDate);

  List<UserDailyMission> findAllByUserIdAndDailyMissionIdOrderByIdAsc(UUID userId,
      Long missionId);

  @Modifying
  @Query("DELETE FROM UserDailyMission udm WHERE udm.dailyMission.id = :missionId")
  void deleteByDailyMissionId(@Param("missionId") Long missionId);

  @Modifying
  @Query(value = """
      INSERT INTO user_daily_mission (
          user_id, mission_id, current_progress, is_completed, reward_claimed
      )
      VALUES (:userId, :missionId, 0, FALSE, FALSE)
      ON CONFLICT (user_id, mission_id) DO NOTHING
      """, nativeQuery = true)
  void insertIfMissing(@Param("userId") UUID userId, @Param("missionId") Long missionId);

  @Query("SELECT COALESCE(SUM(dm.rewardPoints), 0) FROM UserDailyMission udm "
      + "JOIN udm.dailyMission dm WHERE udm.userId = :userId "
      + "AND udm.isCompleted = true AND udm.rewardClaimed = true")
  Integer calculateTotalRewardPoints(@Param("userId") UUID userId);
}
