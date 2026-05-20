package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import java.util.List;

public interface DailyMissionService {
  DailyMission create(DailyMission mission);

  List<DailyMission> findAll();

  DailyMission update(Long id, DailyMission mission);

  void delete(Long id);

  void rotateDailyMissions();
}