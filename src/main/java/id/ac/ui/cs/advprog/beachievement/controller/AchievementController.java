package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.dto.ApiResponse;
import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.AchievementRequest;
import id.ac.ui.cs.advprog.beachievement.service.AchievementService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/achievements")
@PreAuthorize("hasRole('ADMIN')")
public class AchievementController {

  private final AchievementService achievementService;

  public AchievementController(AchievementService service) {
    this.achievementService = service;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Achievement>> createAchievement(
      @Valid @RequestBody AchievementRequest request) {
    Achievement achievement = new Achievement();
    achievement.setTitle(request.getTitle());
    achievement.setDescription(request.getDescription());
    achievement.setMilestone(request.getMilestone());

    Achievement saved = achievementService.create(achievement);
    return new ResponseEntity<>(
        ApiResponse.success("Achievement created successfully", saved),
        HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<Achievement>>> getAllAchievements() {
    return ResponseEntity.ok(
        ApiResponse.success("Achievements retrieved successfully", achievementService.findAll()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteAchievement(@PathVariable Long id) {
    achievementService.delete(id);
    return ResponseEntity.ok(ApiResponse.success("Achievement deleted successfully", null));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Achievement>> updateAchievement(
      @PathVariable Long id,
      @Valid @RequestBody AchievementRequest request) {
    Achievement achievement = new Achievement();
    achievement.setTitle(request.getTitle());
    achievement.setDescription(request.getDescription());
    achievement.setMilestone(request.getMilestone());

    Achievement updated = achievementService.update(id, achievement);
    if (updated == null) {
      return new ResponseEntity<>(ApiResponse.error("Achievement not found"), HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(ApiResponse.success("Achievement updated successfully", updated));
  }
}