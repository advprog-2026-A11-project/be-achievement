package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.service.AchievementService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievement")
public class AchievementController {
  @Autowired
  private AchievementService achievementService;

  @GetMapping
  public ResponseEntity<List<Achievement>> getAllAchievements(){
    return ResponseEntity.ok(achievementService.findAll());
  }

  @PostMapping
  public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement){
    return ResponseEntity.ok(achievementService.create(achievement));
  }
}
