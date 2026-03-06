package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentProgressController {

  @Autowired
  private UserDailyMissionRepository userDailyMissionRepository;

  @Autowired
  private AchievementRepository achievementRepository;

  @GetMapping("/achievements")
  public ResponseEntity<List<Achievement>> getAvailableAchievements() {
    return ResponseEntity.ok(achievementRepository.findAll());
  }

  @GetMapping("/daily-missions/{userId}")
  public ResponseEntity<List<UserDailyMission>> getUserMissions(@PathVariable String userId) {
    List<UserDailyMission> progressList = userDailyMissionRepository.findByUserId(userId);
    return ResponseEntity.ok(progressList);
  }

  @PostMapping("/daily-missions/update-dummy")
  public ResponseEntity<UserDailyMission> updateProgressDummy(
      @RequestBody UserDailyMission userMission) {
    return ResponseEntity.ok(userDailyMissionRepository.save(userMission));
  }
}