package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AchievementServiceImplTests {

  @Mock
  private AchievementRepository achievementRepository;

  @InjectMocks
  private AchievementServiceImpl achievementService;

  private Achievement achievement;

  @BeforeEach
  void setUp() {
    achievement = new Achievement();
    achievement.setId(1L);
    achievement.setTitle("Test Achievement");
  }

  @Test
  void testFindAll() {
    when(achievementRepository.findAll()).thenReturn(Arrays.asList(achievement));

    List<Achievement> result = achievementService.findAll();

    assertEquals(1, result.size());
    assertEquals("Test Achievement", result.get(0).getTitle());
  }

  @Test
  void testCreateAchievement() {
    when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);

    Achievement created = achievementService.create(achievement);

    assertNotNull(created);
    assertEquals("Test Achievement", created.getTitle());
    verify(achievementRepository, times(1)).save(any(Achievement.class));
  }

  @Test
  void testDeleteAchievement() {
    achievementService.delete(1L);
    verify(achievementRepository, times(1)).deleteById(1L);
  }
}