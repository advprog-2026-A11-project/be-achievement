package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.service.AchievementService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/achievement")
public class AchievementController {
  @Autowired
  private AchievementService achievementService;

  @GetMapping
  public ResponseEntity<List<Achievement>> getAllAchievements() {
    return ResponseEntity.ok(achievementService.findAll());
  }

  @PostMapping
  public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
    return ResponseEntity.ok(achievementService.create(achievement));
  }
}