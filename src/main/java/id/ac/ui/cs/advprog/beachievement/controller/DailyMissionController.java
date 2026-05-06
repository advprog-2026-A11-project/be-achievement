package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.dto.ApiResponse;
import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.DailyMissionRequest;
import id.ac.ui.cs.advprog.beachievement.service.DailyMissionService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/daily-missions")
@PreAuthorize("hasRole('ADMIN')")
public class DailyMissionController {

  private final DailyMissionService dailyMissionService;

  public DailyMissionController(DailyMissionService dailyMissionService) {
    this.dailyMissionService = dailyMissionService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<DailyMission>> createDailyMission(
      @Valid @RequestBody DailyMissionRequest request) {
    DailyMission mission = new DailyMission();
    mission.setTitle(request.getTitle());
    mission.setDescription(request.getDescription());
    mission.setTargetMilestone(request.getTargetMilestone());
    mission.setRewardPoints(request.getRewardPoints());
    mission.setActiveDate(request.getActiveDate() != null ? request.getActiveDate() : LocalDate.now());

    DailyMission saved = dailyMissionService.create(mission);
    return new ResponseEntity<>(
        ApiResponse.success("Daily mission created successfully", saved),
        HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<DailyMission>>> getAllDailyMissions() {
    return ResponseEntity.ok(
        ApiResponse.success("Daily missions retrieved successfully", dailyMissionService.findAll()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<DailyMission>> updateDailyMission(
      @PathVariable Long id,
      @Valid @RequestBody DailyMissionRequest request) {
    DailyMission mission = new DailyMission();
    mission.setTitle(request.getTitle());
    mission.setDescription(request.getDescription());
    mission.setTargetMilestone(request.getTargetMilestone());
    mission.setRewardPoints(request.getRewardPoints());
    mission.setActiveDate(request.getActiveDate());

    DailyMission updated = dailyMissionService.update(id, mission);
    if (updated == null) {
      return new ResponseEntity<>(ApiResponse.error("Daily mission not found"), HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(ApiResponse.success("Daily mission updated successfully", updated));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteDailyMission(@PathVariable Long id) {
    dailyMissionService.delete(id);
    return ResponseEntity.ok(ApiResponse.success("Daily mission deleted successfully", null));
  }
}