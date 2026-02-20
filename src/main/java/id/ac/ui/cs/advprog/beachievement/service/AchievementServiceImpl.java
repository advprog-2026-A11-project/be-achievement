package id.ac.ui.cs.advprog.beachievement.service;

import java.util.List;
import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementServiceImpl implements AchievementService {
  @Autowired
  private AchievementRepository achievementRepository;

  @Override
  public List<Achievement> findAll() {
    return achievementRepository.findAll();
  }

  @Override
  public Achievement create(Achievement achievement) {
    return achievementRepository.save(achievement);
  }

  @Override
  public Achievement findById(String id) {
    return achievementRepository.findById(id).orElse(null);
  }
}