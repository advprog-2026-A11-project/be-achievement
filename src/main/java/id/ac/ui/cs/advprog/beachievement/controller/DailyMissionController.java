package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.service.DailyMissionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/daily-missions")
public class DailyMissionController {
  @Autowired
  private DailyMissionService dailyMissionService;

  @GetMapping
  public ResponseEntity<List<DailyMission>> getAllMissions() {
    return ResponseEntity.ok(dailyMissionService.findAll());
  }

  @PostMapping
  public ResponseEntity<DailyMission> createMission(@RequestBody DailyMission mission) {
    return ResponseEntity.ok(dailyMissionService.create(mission));
  }

  @PutMapping("/{id}")
  public ResponseEntity<DailyMission> updateMission(
      @PathVariable String id,
      @RequestBody DailyMission mission) {
    return ResponseEntity.ok(dailyMissionService.update(id, mission));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMission(@PathVariable String id) {
    dailyMissionService.delete(id);
    return ResponseEntity.ok().build();
  }
}