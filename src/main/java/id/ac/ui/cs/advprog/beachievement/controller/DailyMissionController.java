package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.service.DailyMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daily-missions")
public class DailyMissionController {

  @Autowired
  private DailyMissionService dailyMissionService;

  @PostMapping("/create")
  public ResponseEntity<DailyMission> create(@RequestBody DailyMission mission) {
    return ResponseEntity.ok(dailyMissionService.create(mission));
  }

  @GetMapping("/all")
  public ResponseEntity<List<DailyMission>> getAll() {
    return ResponseEntity.ok(dailyMissionService.findAll());
  }
}