package id.ac.ui.cs.advprog.beachievement.repository;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, String> {
}