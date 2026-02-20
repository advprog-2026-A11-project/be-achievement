package id.ac.ui.cs.advprog.beachievement.service;

import java.util.List;
import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyMissionServiceImpl implements DailyMissionService {
  @Autowired
  private DailyMissionRepository dailyMissionRepository;

  @Override
  public List<DailyMission> findAll() {
    return dailyMissionRepository.findAll();
  }

  @Override
  public DailyMission create(DailyMission mission) {
    return dailyMissionRepository.save(mission);
  }

  @Override
  public DailyMission update(String id, DailyMission mission) {
    DailyMission existing = dailyMissionRepository.findById(id).orElse(null);
    if (existing != null) {
      existing.setTitle(mission.getTitle());
      existing.setRequirement(mission.getRequirement());
      existing.setActive(mission.isActive());
      return dailyMissionRepository.save(existing);
    }
    return null;
  }

  @Override
  public void delete(String id) {
    dailyMissionRepository.deleteById(id);
  }
}