package id.ac.ui.cs.advprog.beachievement.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_quiz_counts")
@Getter
@Setter
public class UserQuizCount {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "quiz_count", nullable = false)
    private Integer quizCount = 0;
    @Column(name = "last_processed_event_id")
    private String lastProcessedEventId;
}