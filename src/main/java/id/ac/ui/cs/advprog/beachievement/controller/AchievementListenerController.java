package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.service.AchievementListenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class AchievementListenerController {

    @Autowired
    private AchievementListenerService achievementListenerService;

    @PostMapping("/quiz-completed")
    public ResponseEntity<String> receiveQuizCompletedEvent(@RequestBody QuizCompletedEvent event) {
        achievementListenerService.processQuizCompleted(event);
        return ResponseEntity.ok("Quiz completed event received successfully");
    }
}
