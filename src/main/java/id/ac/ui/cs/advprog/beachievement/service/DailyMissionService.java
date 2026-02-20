package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import java.util.List;

public interface DailyMissionService {
  List<DailyMission> findAll();

  DailyMission create(DailyMission mission);

  DailyMission update(String id, DailyMission mission);

  void delete(String id);
}