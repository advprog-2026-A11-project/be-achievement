package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.dto.ApiResponse;
import id.ac.ui.cs.advprog.beachievement.dto.UserAchievementResponse;
import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.service.UserAchievementService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/achievements")
public class StudentAchievementController {

  private final UserAchievementService userAchievementService;

  public StudentAchievementController(UserAchievementService userAchievementService) {
    this.userAchievementService = userAchievementService;
  }

  private UUID getAuthenticatedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
    }
    try {
      return UUID.fromString(authentication.getName());
    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token user ID format");
    }
  }

  @GetMapping("/me")
  public ResponseEntity<ApiResponse<List<UserAchievement>>> getMyAchievements() {
    UUID userId = getAuthenticatedUserId();
    List<UserAchievement> achievements = userAchievementService.getUnlockedAchievements(userId);
    return ResponseEntity.ok(
        ApiResponse.success("Achievements retrieved successfully", achievements));
  }

  @GetMapping("/{userId}/public")
  public ResponseEntity<ApiResponse<List<UserAchievement>>> getPublicAchievements(
      @PathVariable UUID userId) {
    List<UserAchievement> achievements = userAchievementService.getPublicAchievements(userId);
    return ResponseEntity.ok(
        ApiResponse.success("Public achievements retrieved successfully", achievements));
  }

  @GetMapping("/{userId}/completed")
  public ResponseEntity<ApiResponse<List<UserAchievementResponse>>> getCompletedAchievements(
      @PathVariable UUID userId) {
    List<UserAchievementResponse> achievements = userAchievementService
        .getUnlockedAchievements(userId)
        .stream()
        .map(UserAchievementResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        ApiResponse.success("Completed achievements retrieved successfully", achievements));
  }

  @PutMapping("/featured/{achievementId}")
  public ResponseEntity<ApiResponse<Void>> setFeaturedAchievement(
      @PathVariable Long achievementId,
      @RequestParam boolean showcased) {
    UUID userId = getAuthenticatedUserId();
    userAchievementService.setFeaturedAchievement(userId, achievementId, showcased);
    return ResponseEntity.ok(ApiResponse.success("Featured achievement status updated", null));
  }
}
