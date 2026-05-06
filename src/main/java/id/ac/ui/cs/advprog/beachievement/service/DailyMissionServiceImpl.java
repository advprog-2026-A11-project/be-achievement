package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyMissionServiceImpl implements DailyMissionService {
  private final DailyMissionRepository dailyMissionRepository;

  public DailyMissionServiceImpl(DailyMissionRepository dailyMissionRepository) {
    this.dailyMissionRepository = dailyMissionRepository;
  }

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
      mission.setDescription(updatedData.getDescription());
      mission.setTargetMilestone(updatedData.getTargetMilestone());
      mission.setRewardPoints(updatedData.getRewardPoints());
      mission.setActiveDate(updatedData.getActiveDate());
      mission.setMissionType(updatedData.getMissionType());
      mission.setActive(updatedData.isActive());
      return dailyMissionRepository.save(mission);
    }).orElse(null);
  }

  @Override
  public void delete(Long id) {
    dailyMissionRepository.deleteById(id);
  }
}