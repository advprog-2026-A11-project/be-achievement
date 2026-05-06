package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.dto.ApiResponse;
import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.service.AchievementListenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class AchievementListenerController {
  private final AchievementListenerService achievementListenerService;

  public AchievementListenerController(AchievementListenerService achievementListenerService) {
    this.achievementListenerService = achievementListenerService;
  }

  @PostMapping("/quiz-completed")
  public ResponseEntity<ApiResponse<Void>> receiveQuizCompletedEvent(@RequestBody QuizCompletedEvent event) {
    achievementListenerService.processQuizCompleted(event);
    return ResponseEntity.ok(ApiResponse.success("Quiz completed event received successfully", null));
  }
}