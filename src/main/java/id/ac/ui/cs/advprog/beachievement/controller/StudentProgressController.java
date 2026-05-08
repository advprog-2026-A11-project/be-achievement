package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.dto.ApiResponse;
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
  public ResponseEntity<ApiResponse<List<UserDailyMission>>> getMissions(
      @PathVariable UUID userId) {
    List<UserDailyMission> missions = studentProgressService.getStudentMissions(userId);
    return ResponseEntity.ok(
        ApiResponse.success("Student missions retrieved successfully", missions));
  }

  @PutMapping("/{userId}/missions/{missionId}/progress")
  public ResponseEntity<ApiResponse<UserDailyMission>> updateProgress(
      @PathVariable UUID userId,
      @PathVariable Long missionId,
      @RequestBody Map<String, Integer> body) {
    Integer progress = body.get("progress");
    if (progress == null) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error("Request body must contain a valid 'progress' integer"));
    }
    UserDailyMission updated = studentProgressService.updateProgress(userId, missionId, progress);
    return ResponseEntity.ok(ApiResponse.success("Mission progress updated successfully", updated));
  }
}