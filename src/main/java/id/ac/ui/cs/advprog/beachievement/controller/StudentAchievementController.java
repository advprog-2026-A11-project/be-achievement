package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.dto.ApiResponse;
import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.service.UserAchievementService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievements")
public class StudentAchievementController {

  private final UserAchievementService userAchievementService;

  public StudentAchievementController(UserAchievementService userAchievementService) {
    this.userAchievementService = userAchievementService;
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<List<UserAchievement>>> getMyAchievements(
      @RequestAttribute("userId") String userId) {
    List<UserAchievement> achievements = userAchievementService
        .getUnlockedAchievements(UUID.fromString(userId));
    return ResponseEntity.ok(ApiResponse.success("Achievements retrieved successfully", achievements));
  }

  @GetMapping("/{userId}/public")
  public ResponseEntity<ApiResponse<List<UserAchievement>>> getPublicAchievements(
      @PathVariable UUID userId) {
    List<UserAchievement> achievements = userAchievementService.getPublicAchievements(userId);
    return ResponseEntity.ok(ApiResponse.success("Public achievements retrieved successfully", achievements));
  }

  @PutMapping("/featured/{achievementId}")
  public ResponseEntity<ApiResponse<Void>> setFeaturedAchievement(
      @RequestAttribute("userId") String userId,
      @PathVariable Long achievementId,
      @RequestParam boolean showcased) {
    userAchievementService.setFeaturedAchievement(UUID.fromString(userId), achievementId, showcased);
    return ResponseEntity.ok(ApiResponse.success("Featured achievement status updated", null));
  }
}