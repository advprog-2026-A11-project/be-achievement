package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DailyMissionServiceImpl implements DailyMissionService {
  @Autowired
  private DailyMissionRepository dailyMissionRepository;

  @Override
  public DailyMission create(DailyMission mission) {
    return dailyMissionRepository.save(mission);
  }

  @Override
  public List<DailyMission> findAll() {
    return dailyMissionRepository.findAll();
  }

  @Override
  public DailyMission update(Long id, DailyMission updatedData) {
    return dailyMissionRepository.findById(id).map(mission -> {
      mission.setTitle(updatedData.getTitle());
      return dailyMissionRepository.save(mission);
    }).orElse(null);
  }

  @Override
  public void delete(Long id) {
    dailyMissionRepository.deleteById(id);
  }
}