package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AchievementServiceImpl implements AchievementService {

  private final AchievementRepository achievementRepository;

  public AchievementServiceImpl(AchievementRepository achievementRepository) {
    this.achievementRepository = achievementRepository;
  }

  @Override
  public List<Achievement> findAll() {
    return achievementRepository.findAll();
  }

  @Override
  public Achievement create(Achievement achievement) {
    return achievementRepository.save(achievement);
  }

  @Override
  public Achievement findById(Long id) {
    return achievementRepository.findById(id).orElse(null);
  }

  @Override
  public void delete(Long id) {
    achievementRepository.deleteById(id);
  }
}