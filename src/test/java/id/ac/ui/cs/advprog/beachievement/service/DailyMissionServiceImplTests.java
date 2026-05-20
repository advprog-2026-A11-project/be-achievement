package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DailyMissionServiceImplTests {

  @Mock
  private DailyMissionRepository dailyMissionRepository;

  @InjectMocks
  private DailyMissionServiceImpl dailyMissionService;

  private DailyMission mission;

  @BeforeEach
  void setUp() {
    mission = new DailyMission();
    mission.setId(1L);
    mission.setTitle("Membaca Berita");
    mission.setDescription("Baca 1 berita hari ini");
    mission.setRewardPoints(50);
  }

  @Test
  void testFindAllMissions() {
    when(dailyMissionRepository.findAll()).thenReturn(Arrays.asList(mission));

    List<DailyMission> result = dailyMissionService.findAll();

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals("Membaca Berita", result.get(0).getTitle());
    verify(dailyMissionRepository, times(1)).findAll();
  }

  @Test
  void testCreateMission() {
    when(dailyMissionRepository.save(any(DailyMission.class))).thenReturn(mission);
    DailyMission created = dailyMissionService.create(mission);
    assertNotNull(created);
    assertEquals("Membaca Berita", created.getTitle());
    verify(dailyMissionRepository, times(1)).save(any(DailyMission.class));
  }

  @Test
  void testUpdateMission() {
    when(dailyMissionRepository.findById(1L)).thenReturn(Optional.of(mission));
    when(dailyMissionRepository.save(any(DailyMission.class))).thenReturn(mission);

    DailyMission updatedData = new DailyMission();
    updatedData.setTitle("Misi Baru");
    updatedData.setDescription("Deskripsi Baru");

    DailyMission result = dailyMissionService.update(1L, updatedData);

    assertNotNull(result);
    assertEquals("Misi Baru", result.getTitle());
    verify(dailyMissionRepository, times(1)).findById(1L);
    verify(dailyMissionRepository, times(1)).save(any(DailyMission.class));
  }

  @Test
  void testUpdateMissionNotFound() {
    when(dailyMissionRepository.findById(99L)).thenReturn(Optional.empty());

    DailyMission result = dailyMissionService.update(99L, new DailyMission());

    assertNull(result);
  }

  @Test
  void testDeleteMission() {
    doNothing().when(dailyMissionRepository).deleteById(1L);

    dailyMissionService.delete(1L);

    verify(dailyMissionRepository, times(1)).deleteById(1L);
  }

  @Test
  void testRotateDailyMissionsWithEmptyDatabase() {
    when(dailyMissionRepository.findAll()).thenReturn(Collections.emptyList());
    when(dailyMissionRepository.save(any(DailyMission.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    dailyMissionService.rotateDailyMissions();

    verify(dailyMissionRepository, atLeast(4)).save(any(DailyMission.class));
  }

  @Test
  void testRotateDailyMissionsWithExistingMissions() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    DailyMission oldMission = new DailyMission();
    oldMission.setId(2L);
    oldMission.setTitle("Misi Kemarin");
    oldMission.setActiveDate(yesterday);
    oldMission.setActive(true);

    List<DailyMission> existing = new ArrayList<>();
    existing.add(oldMission);

    List<DailyMission> savedSnapshots = new ArrayList<>();
    when(dailyMissionRepository.findAll()).thenReturn(existing);
    when(dailyMissionRepository.save(any(DailyMission.class)))
        .thenAnswer(invocation -> {
          DailyMission arg = invocation.getArgument(0);
          DailyMission copy = new DailyMission();
          copy.setId(arg.getId());
          copy.setActive(arg.isActive());
          copy.setActiveDate(arg.getActiveDate());
          savedSnapshots.add(copy);
          return arg;
        });

    dailyMissionService.rotateDailyMissions();

    verify(dailyMissionRepository, atLeastOnce()).save(any(DailyMission.class));

    boolean hasDeactivated = savedSnapshots.stream()
        .anyMatch(m -> m.getId().equals(2L) && !m.isActive());
    assertTrue(hasDeactivated, "The yesterday's daily mission should be deactivated first");
  }
}