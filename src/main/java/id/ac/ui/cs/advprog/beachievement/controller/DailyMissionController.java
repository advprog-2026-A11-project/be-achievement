package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.DailyMissionRequest;
import id.ac.ui.cs.advprog.beachievement.service.DailyMissionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/daily-missions")
public class DailyMissionController {

  private final DailyMissionService dailyMissionService;

  public DailyMissionController(DailyMissionService dailyMissionService) {
    this.dailyMissionService = dailyMissionService;
  }

  @PostMapping
  public ResponseEntity<DailyMission> create(@RequestBody DailyMissionRequest request) {
    DailyMission mission = new DailyMission();
    mission.setTitle(request.getTitle());
    mission.setDescription(request.getDescription());
    mission.setTargetMilestone(request.getTargetMilestone());
    mission.setRewardPoints(request.getRewardPoints());
    mission.setActiveDate(request.getActiveDate());

    return new ResponseEntity<>(dailyMissionService.create(mission), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<DailyMission>> getAll() {
    return ResponseEntity.ok(dailyMissionService.findAll());
  }

  @PutMapping("/{id}")
  public ResponseEntity<DailyMission> update(
      @PathVariable Long id, @RequestBody DailyMissionRequest request) {
    DailyMission mission = new DailyMission();
    mission.setTitle(request.getTitle());
    mission.setDescription(request.getDescription());
    mission.setTargetMilestone(request.getTargetMilestone());
    mission.setRewardPoints(request.getRewardPoints());
    mission.setActiveDate(request.getActiveDate());

    return ResponseEntity.ok(dailyMissionService.update(id, mission));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable Long id) {
    dailyMissionService.delete(id);
    return ResponseEntity.ok("Daily Mission deleted successfully!");
  }
}