package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.service.StudentProgressService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student-progress")
public class StudentProgressController {

  private final StudentProgressService studentProgressService;

  public StudentProgressController(StudentProgressService studentProgressService) {
    this.studentProgressService = studentProgressService;
  }

  @GetMapping("/{studentId}/missions")
  public ResponseEntity<List<DailyMission>> getMissions(@PathVariable Long studentId) {
    return ResponseEntity.ok(studentProgressService.getStudentMissions(studentId));
  }
}