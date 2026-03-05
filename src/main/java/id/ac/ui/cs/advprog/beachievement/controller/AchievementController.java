package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.service.AchievementService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/achievements") // Tambahkan /admin dan hapus method action
public class AchievementController {

  private final AchievementService achievementService;

  public AchievementController(AchievementService service) {
    this.achievementService = service;
  }

  // URL menjadi: POST /api/admin/achievements
  @PostMapping
  public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
    return new ResponseEntity<>(achievementService.create(achievement), HttpStatus.CREATED);
  }

  // URL menjadi: GET /api/admin/achievements
  @GetMapping
  public ResponseEntity<List<Achievement>> getAllAchievements() {
    return ResponseEntity.ok(achievementService.findAll());
  }

  // URL menjadi: DELETE /api/admin/achievements/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAchievement(@PathVariable Long id) {
    achievementService.delete(id);
    return ResponseEntity.ok("Achievement deleted successfully!");
  }
}