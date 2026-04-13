package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.service.StudentProgressService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-progress")
public class StudentProgressController {

  private final StudentProgressService studentProgressService;

  public StudentProgressController(StudentProgressService studentProgressService) {
    this.studentProgressService = studentProgressService;
  }

  @GetMapping("/{userId}/missions")
  public ResponseEntity<List<UserDailyMission>> getMissions(@PathVariable UUID userId) {
    return ResponseEntity.ok(studentProgressService.getStudentMissions(userId));
  }

  @PutMapping("/{userId}/missions/{missionId}/progress")
  public ResponseEntity<UserDailyMission> updateProgress(
      @PathVariable UUID userId,
      @PathVariable Long missionId,
      @RequestBody Map<String, Integer> body) {
    Integer progress = body.get("progress");
    return ResponseEntity.ok(studentProgressService.updateProgress(userId, missionId, progress));
  }
}