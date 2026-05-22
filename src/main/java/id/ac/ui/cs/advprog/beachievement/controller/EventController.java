package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.dto.ApiResponse;
import id.ac.ui.cs.advprog.beachievement.model.ClanPromotedEvent;
import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.service.AchievementListenerService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@Slf4j
public class EventController {

  private final AchievementListenerService achievementListenerService;

  public EventController(AchievementListenerService achievementListenerService) {
    this.achievementListenerService = achievementListenerService;
  }

  @PostMapping("/quiz-completed")
  public ResponseEntity<ApiResponse<String>> handleQuizCompletedEvent(
      @RequestBody QuizCompletedEvent event) {
    log.info("Received quiz completed event via REST: {}", event);

    if (event.getUserId() == null) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error("User ID must not be null"));
    }

    // Standardize eventId generation if not present to support database auditing
    if (event.getEventId() == null || event.getEventId().isBlank()) {
      event.setEventId(UUID.randomUUID().toString());
    }

    try {
      achievementListenerService.processQuizCompleted(event);
      log.info("Successfully processed quiz completed event for user: {}", event.getUserId());
      return ResponseEntity.ok(
          ApiResponse.success("Quiz completed event processed successfully", "SUCCESS"));
    } catch (Exception e) {
      log.error("Failed to process quiz completed event", e);
      String errMsg = "Failed to process quiz completed event: " + e.getMessage();
      return ResponseEntity.internalServerError()
          .body(ApiResponse.error(errMsg));
    }
  }

  @PostMapping("/clan-promoted")
  public ResponseEntity<ApiResponse<String>> handleClanPromotedEvent(
      @RequestBody ClanPromotedEvent event) {
    log.info("Received clan promoted event via REST: {}", event);

    if (event.getTier() != null && !"Diamond".equalsIgnoreCase(event.getTier())) {
      return ResponseEntity.ok(
          ApiResponse.success("Clan promotion event ignored because tier is not Diamond",
              "IGNORED"));
    }

    if (event.getEventId() == null || event.getEventId().isBlank()) {
      event.setEventId(UUID.randomUUID().toString());
    }

    if (event.getUserIds() == null || event.getUserIds().isEmpty()) {
      log.warn("Clan promoted event for clan {} has no user IDs; acknowledging without unlock",
          event.getClanId());
      return ResponseEntity.ok(
          ApiResponse.success("Clan promoted event acknowledged without member unlocks",
              "NO_USERS"));
    }

    try {
      achievementListenerService.processClanPromoted(event);
      log.info("Successfully processed clan promoted event for clan: {}", event.getClanId());
      return ResponseEntity.ok(
          ApiResponse.success("Clan promoted event processed successfully", "SUCCESS"));
    } catch (Exception e) {
      log.error("Failed to process clan promoted event", e);
      String errMsg = "Failed to process clan promoted event: " + e.getMessage();
      return ResponseEntity.internalServerError()
          .body(ApiResponse.error(errMsg));
    }
  }
}
