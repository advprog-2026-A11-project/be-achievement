package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserAchievementRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserAchievementServiceImplTest {

  @Mock
  private UserAchievementRepository userAchievementRepository;

  @Mock
  private AchievementRepository achievementRepository;

  @InjectMocks
  private UserAchievementServiceImpl userAchievementService;

  private UUID userId;
  private UserAchievement userAchievement;
  private Achievement achievement;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    
    achievement = new Achievement();
    achievement.setId(1L);
    achievement.setTitle("First Blood");
    achievement.setMilestone(1);

    userAchievement = new UserAchievement();
    userAchievement.setId(1L);
    userAchievement.setUserId(userId);
    userAchievement.setAchievement(achievement);
    userAchievement.setShowcased(false);
  }

  @Test
  void testGetUnlockedAchievements() {
    when(userAchievementRepository.findByUserId(userId)).thenReturn(List.of(userAchievement));

    List<UserAchievement> results = userAchievementService.getUnlockedAchievements(userId);

    assertEquals(1, results.size());
    assertEquals("First Blood", results.get(0).getAchievement().getTitle());
    verify(userAchievementRepository).findByUserId(userId);
  }

  @Test
  void testGetPublicAchievements() {
    userAchievement.setShowcased(true);
    when(userAchievementRepository.findByUserIdAndIsShowcasedTrue(userId))
        .thenReturn(List.of(userAchievement));

    List<UserAchievement> results = userAchievementService.getPublicAchievements(userId);

    assertEquals(1, results.size());
    assertTrue(results.get(0).isShowcased());
    verify(userAchievementRepository).findByUserIdAndIsShowcasedTrue(userId);
  }

  @Test
  void testSetFeaturedAchievementSuccess() {
    when(userAchievementRepository.findByUserIdAndAchievementId(userId, 1L))
        .thenReturn(Optional.of(userAchievement));

    userAchievementService.setFeaturedAchievement(userId, 1L, true);

    assertTrue(userAchievement.isShowcased());
    verify(userAchievementRepository).save(userAchievement);
  }

  @Test
  void testSetFeaturedAchievementNotFound() {
    when(userAchievementRepository.findByUserIdAndAchievementId(userId, 2L))
        .thenReturn(Optional.empty());

    userAchievementService.setFeaturedAchievement(userId, 2L, true);

    verify(userAchievementRepository, never()).save(any());
  }

  @Test
  void testCheckAndUnlockAchievementsUnlockNew() {
    when(achievementRepository.findAll()).thenReturn(List.of(achievement));
    when(userAchievementRepository.existsByUserIdAndAchievementId(userId, 1L)).thenReturn(false);

    userAchievementService.checkAndUnlockAchievements(userId, 1);

    verify(userAchievementRepository).save(any(UserAchievement.class));
  }

  @Test
  void testCheckAndUnlockAchievementsAlreadyUnlocked() {
    when(achievementRepository.findAll()).thenReturn(List.of(achievement));
    when(userAchievementRepository.existsByUserIdAndAchievementId(userId, 1L)).thenReturn(true);

    userAchievementService.checkAndUnlockAchievements(userId, 1);

    verify(userAchievementRepository, never()).save(any(UserAchievement.class));
  }

  @Test
  void testCheckAndUnlockAchievementsMilestoneNotReached() {
    when(achievementRepository.findAll()).thenReturn(List.of(achievement));

    userAchievementService.checkAndUnlockAchievements(userId, 0);

    verify(userAchievementRepository, never()).existsByUserIdAndAchievementId(any(), any());
    verify(userAchievementRepository, never()).save(any(UserAchievement.class));
  }
}
