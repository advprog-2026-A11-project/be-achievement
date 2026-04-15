package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.AchievementRequest;
import id.ac.ui.cs.advprog.beachievement.service.AchievementService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/achievements")
public class AchievementController {

  private final AchievementService achievementService;

  public AchievementController(AchievementService service) {
    this.achievementService = service;
  }

  @PostMapping
  public ResponseEntity<Achievement> createAchievement(@RequestBody AchievementRequest request) {
    Achievement achievement = new Achievement();
    achievement.setTitle(request.getTitle());
    achievement.setDescription(request.getDescription());
    achievement.setMilestone(request.getMilestone());

    return new ResponseEntity<>(achievementService.create(achievement), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Achievement>> getAllAchievements() {
    return ResponseEntity.ok(achievementService.findAll());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAchievement(@PathVariable Long id) {
    achievementService.delete(id);
    return ResponseEntity.ok("Achievement deleted successfully!");
  }

  @PutMapping("/{id}")
  public ResponseEntity<Achievement> updateAchievement(
      @PathVariable Long id,
      @RequestBody AchievementRequest request) {
    Achievement achievement = new Achievement();
    achievement.setTitle(request.getTitle());
    achievement.setDescription(request.getDescription());
    achievement.setMilestone(request.getMilestone());

    Achievement updated = achievementService.update(id, achievement);
    if (updated == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updated);
  }
}