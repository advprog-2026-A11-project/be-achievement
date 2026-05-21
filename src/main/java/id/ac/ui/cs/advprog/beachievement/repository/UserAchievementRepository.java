package id.ac.ui.cs.advprog.beachievement.repository;

import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
  List<UserAchievement> findByUserId(UUID userId);

  List<UserAchievement> findByUserIdAndIsShowcasedTrue(UUID userId);

  Optional<UserAchievement> findByUserIdAndAchievementId(UUID userId, Long achievementId);

  boolean existsByUserIdAndAchievementId(UUID userId, Long achievementId);

  @Modifying
  @Query("DELETE FROM UserAchievement ua WHERE ua.achievement.id = :achievementId")
  void deleteByAchievementId(@Param("achievementId") Long achievementId);
}
