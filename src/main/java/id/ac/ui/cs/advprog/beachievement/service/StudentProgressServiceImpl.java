package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentProgressServiceImpl implements StudentProgressService {

  private final DailyMissionRepository dailyMissionRepository;

  public StudentProgressServiceImpl(DailyMissionRepository dailyMissionRepository) {
    this.dailyMissionRepository = dailyMissionRepository;
  }

  @Override
  public List<DailyMission> getStudentMissions(Long studentId) {
    return dailyMissionRepository.findAll();
  }
}