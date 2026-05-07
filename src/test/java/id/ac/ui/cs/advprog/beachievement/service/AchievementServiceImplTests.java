package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.repository.AchievementRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

  @Test
  void testFindByIdFound() {
    when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
    Achievement found = achievementService.findById(1L);
    assertNotNull(found);
    assertEquals("Test Achievement", found.getTitle());
  }

  @Test
  void testFindByIdNotFound() {
    when(achievementRepository.findById(1L)).thenReturn(Optional.empty());
    Achievement found = achievementService.findById(1L);
    assertNull(found);
  }

  @Test
  void testUpdateFound() {
    Achievement updatedInfo = new Achievement();
    updatedInfo.setTitle("Updated Title");
    updatedInfo.setDescription("Updated Desc");
    updatedInfo.setMilestone(10);

    when(achievementRepository.findById(1L)).thenReturn(Optional.of(achievement));
    when(achievementRepository.save(any(Achievement.class))).thenReturn(achievement);

    Achievement result = achievementService.update(1L, updatedInfo);
    assertNotNull(result);
    verify(achievementRepository).save(achievement);
  }

  @Test
  void testUpdateNotFound() {
    Achievement updatedInfo = new Achievement();
    when(achievementRepository.findById(1L)).thenReturn(Optional.empty());

    Achievement result = achievementService.update(1L, updatedInfo);
    assertNull(result);
    verify(achievementRepository, never()).save(any());
  }
}