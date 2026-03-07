package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

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