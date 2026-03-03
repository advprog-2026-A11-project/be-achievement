package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.service.AchievementService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

  private final AchievementService achievementService;

  public AchievementController(AchievementService service) {
    this.achievementService = service;
  }

  @PostMapping("/create")
  public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
    return ResponseEntity.ok(achievementService.create(achievement));
  }

  @GetMapping("/all")
  public ResponseEntity<List<Achievement>> getAllAchievements() {
    return ResponseEntity.ok(achievementService.findAll());
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteAchievement(@PathVariable Long id) {
    achievementService.delete(id);
    return ResponseEntity.ok("Achievement deleted successfully!");
  }
}