package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.service.DailyMissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/daily-missions") // Tambahkan /admin
public class DailyMissionController {

  @Autowired
  private DailyMissionService dailyMissionService;

  @PostMapping
  public ResponseEntity<DailyMission> create(@RequestBody DailyMission mission) {
    return new ResponseEntity<>(dailyMissionService.create(mission), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<DailyMission>> getAll() {
    return ResponseEntity.ok(dailyMissionService.findAll());
  }

  @PutMapping("/{id}")
  public ResponseEntity<DailyMission> update(
      @PathVariable Long id, @RequestBody DailyMission mission) {
    return ResponseEntity.ok(dailyMissionService.update(id, mission));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable Long id) {
    dailyMissionService.delete(id);
    return ResponseEntity.ok("Daily Mission deleted successfully!");
  }
}