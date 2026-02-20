package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import java.util.List;

public interface AchievementService {
  List<Achievement> findAll();

  Achievement create(Achievement achievement);

  Achievement findById(String id);
}