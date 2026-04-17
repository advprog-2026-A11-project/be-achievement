package id.ac.ui.cs.advprog.beachievement.repository;

import id.ac.ui.cs.advprog.beachievement.model.UserQuizCount;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuizCountRepository extends JpaRepository<UserQuizCount, UUID> {
    Optional<UserQuizCount> findByUserId(UUID userId);
}