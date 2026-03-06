package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    // Beritahu Mockito: "Jika ada DailyMission apapun yang di-save, kembalikan objek 'mission' yang sudah kita set di atas"
    when(dailyMissionRepository.save(any(DailyMission.class))).thenReturn(mission);

    // Eksekusi service
    DailyMission created = dailyMissionService.create(mission);

    // Validasi
    assertNotNull(created, "Objek yang dibuat tidak boleh null");
    assertEquals("Membaca Berita", created.getTitle(), "Title harus sesuai dengan yang di-mock");
    verify(dailyMissionRepository, times(1)).save(any(DailyMission.class));
  }

  @Test
  void testUpdateMission() {
    // Mock mencari mission lama
    when(dailyMissionRepository.findById(1L)).thenReturn(Optional.of(mission));
    // Mock menyimpan mission yang sudah diupdate
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
    // Skenario jika ID tidak ditemukan untuk menaikkan coverage baris 'orElse(null)'
    when(dailyMissionRepository.findById(99L)).thenReturn(Optional.empty());

    DailyMission result = dailyMissionService.update(99L, new DailyMission());

    assertNull(result);
  }

  @Test
  void testDeleteMission() {
    // Karena deleteById mengembalikan void, kita cukup verifikasi pemanggilannya
    doNothing().when(dailyMissionRepository).deleteById(1L);

    dailyMissionService.delete(1L);

    verify(dailyMissionRepository, times(1)).deleteById(1L);
  }
}