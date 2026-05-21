package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.time.LocalDate;
import java.util.Arrays;
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
class StudentProgressServiceImplTests {

  @Mock
  private UserDailyMissionRepository userDailyMissionRepository;

  @Mock
  private DailyMissionRepository dailyMissionRepository;

  @Mock
  private UserDailyMissionProgressService userDailyMissionProgressService;

  @InjectMocks
  private StudentProgressServiceImpl studentProgressService;

  private UUID userId;
  private DailyMission dailyMission;
  private UserDailyMission userDailyMission;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    
    dailyMission = new DailyMission();
    dailyMission.setId(1L);
    dailyMission.setTargetMilestone(10);
    dailyMission.setActiveDate(LocalDate.now());

    userDailyMission = new UserDailyMission();
    userDailyMission.setId(100L);
    userDailyMission.setUserId(userId);
    userDailyMission.setDailyMission(dailyMission);
    userDailyMission.setCurrentProgress(0);
    userDailyMission.setCompleted(false);
  }

  @Test
  void testGetStudentMissionsWithExistingMission() {
    when(dailyMissionRepository.findByActiveDate(any(LocalDate.class)))
        .thenReturn(Arrays.asList(dailyMission));

    when(userDailyMissionProgressService.getOrCreateUserDailyMission(userId, dailyMission))
        .thenReturn(userDailyMission);

    when(userDailyMissionRepository.findByUserIdAndDailyMissionActiveDate(eq(userId),
        any(LocalDate.class)))
        .thenReturn(Arrays.asList(userDailyMission));

    List<UserDailyMission> missions = studentProgressService.getStudentMissions(userId);

    assertEquals(1, missions.size());
    verify(userDailyMissionProgressService).getOrCreateUserDailyMission(userId, dailyMission);
  }

  @Test
  void testGetStudentMissionsWithoutExistingMission() {
    when(dailyMissionRepository.findByActiveDate(any(LocalDate.class)))
        .thenReturn(Arrays.asList(dailyMission));

    when(userDailyMissionProgressService.getOrCreateUserDailyMission(userId, dailyMission))
        .thenReturn(userDailyMission);

    when(userDailyMissionRepository.findByUserIdAndDailyMissionActiveDate(eq(userId),
        any(LocalDate.class)))
        .thenReturn(Arrays.asList(userDailyMission));

    List<UserDailyMission> missions = studentProgressService.getStudentMissions(userId);

    assertEquals(1, missions.size());
    verify(userDailyMissionProgressService).getOrCreateUserDailyMission(userId, dailyMission);
  }

  @Test
  void testUpdateProgressValidNotCompleted() {
    when(userDailyMissionProgressService.findUserDailyMission(userId, 1L))
        .thenReturn(Optional.of(userDailyMission));
    when(userDailyMissionRepository.save(any(UserDailyMission.class))).thenReturn(userDailyMission);

    UserDailyMission result = studentProgressService.updateProgress(userId, 1L, 5);

    assertEquals(5, result.getCurrentProgress());
    assertFalse(result.isCompleted());
    verify(userDailyMissionRepository).save(userDailyMission);
  }

  @Test
  void testUpdateProgressValidCompleted() {
    when(userDailyMissionProgressService.findUserDailyMission(userId, 1L))
        .thenReturn(Optional.of(userDailyMission));
    when(userDailyMissionRepository.save(any(UserDailyMission.class))).thenReturn(userDailyMission);

    UserDailyMission result = studentProgressService.updateProgress(userId, 1L, 10);

    assertEquals(10, result.getCurrentProgress());
    assertTrue(result.isCompleted());
    verify(userDailyMissionRepository).save(userDailyMission);
  }

  @Test
  void testUpdateProgressNotFound() {
    when(userDailyMissionProgressService.findUserDailyMission(userId, 99L))
        .thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> {
      studentProgressService.updateProgress(userId, 99L, 5);
    });

    verify(userDailyMissionRepository, never()).save(any());
  }

  @Test
  void testCalculateTotalRewardPointsDelegatesToRepository() {
    when(userDailyMissionRepository.calculateTotalRewardPoints(userId)).thenReturn(150);

    Integer totalRewardPoints = studentProgressService.calculateTotalRewardPoints(userId);

    assertEquals(150, totalRewardPoints);
    verify(userDailyMissionRepository).calculateTotalRewardPoints(userId);
  }
}
