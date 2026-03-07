package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import java.util.List;

public interface StudentProgressService {
  List<DailyMission> getStudentMissions(Long studentId);
}