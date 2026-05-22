package id.ac.ui.cs.advprog.beachievement.repository;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
  @Query("SELECT a FROM Achievement a WHERE a.milestone <= :quizCount "
      + "AND (a.milestoneType IS NULL OR a.milestoneType = '' "
      + "OR a.milestoneType = 'QUIZ_COUNT')")
  List<Achievement> findEligibleQuizCountAchievements(@Param("quizCount") int quizCount);

  List<Achievement> findByMilestoneTypeAndMilestoneLessThanEqual(
      String milestoneType, Integer milestone);
}
