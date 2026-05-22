package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserAchievementRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchievementServiceImpl implements AchievementService {

  private final AchievementRepository achievementRepository;
  private final UserAchievementRepository userAchievementRepository;

  public AchievementServiceImpl(
      AchievementRepository achievementRepository,
      UserAchievementRepository userAchievementRepository) {
    this.achievementRepository = achievementRepository;
    this.userAchievementRepository = userAchievementRepository;
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
  public Achievement update(Long id, Achievement achievement) {
    return achievementRepository.findById(id).map(existing -> {
      existing.setTitle(achievement.getTitle());
      existing.setDescription(achievement.getDescription());
      existing.setMilestone(achievement.getMilestone());
      existing.setMilestoneType(achievement.getMilestoneType());
      existing.setIconUrl(achievement.getIconUrl());
      return achievementRepository.save(existing);
    }).orElse(null);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    userAchievementRepository.deleteByAchievementId(id);
    achievementRepository.deleteById(id);
  }
}
