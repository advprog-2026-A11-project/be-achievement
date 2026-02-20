package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
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
class DailyMissionServiceImplTests {

  @Mock
  private DailyMissionRepository dailyMissionRepository;

  @InjectMocks
  private DailyMissionServiceImpl dailyMissionService;

  private DailyMission mission;

  @BeforeEach
  void setUp() {
    mission = new DailyMission();
    mission.setId("m1");
    mission.setTitle("Membaca Berita");
    mission.setRequirement(3);
    mission.setActive(true);
  }

  @Test
  void testFindAllMissions() {
    when(dailyMissionRepository.findAll()).thenReturn(Arrays.asList(mission));
    List<DailyMission> result = dailyMissionService.findAll();
    assertEquals(1, result.size());
    assertEquals("Membaca Berita", result.get(0).getTitle());
  }

  @Test
  void testCreateMission() {
    when(dailyMissionRepository.save(any(DailyMission.class))).thenReturn(mission);
    DailyMission created = dailyMissionService.create(mission);
    assertNotNull(created);
    assertEquals("m1", created.getId());
  }

  @Test
  void testUpdateMission() {
    when(dailyMissionRepository.findById("m1")).thenReturn(Optional.of(mission));
    when(dailyMissionRepository.save(any(DailyMission.class))).thenReturn(mission);

    DailyMission updatedData = new DailyMission();
    updatedData.setTitle("Misi Baru");

    DailyMission result = dailyMissionService.update("m1", updatedData);
    assertNotNull(result);
    verify(dailyMissionRepository).save(any(DailyMission.class));
  }

  @Test
  void testDeleteMission() {
    dailyMissionService.delete("m1");
    verify(dailyMissionRepository, times(1)).deleteById("m1");
  }
}