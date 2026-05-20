package id.ac.ui.cs.advprog.beachievement.scheduler;

import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.beachievement.service.DailyMissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DailyMissionSchedulerTest {

  @Mock
  private DailyMissionService dailyMissionService;

  @InjectMocks
  private DailyMissionScheduler dailyMissionScheduler;

  @Test
  void testRunDailyMissionRotationSuccess() {
    doNothing().when(dailyMissionService).rotateDailyMissions();

    dailyMissionScheduler.runDailyMissionRotation();

    verify(dailyMissionService, times(1)).rotateDailyMissions();
  }

  @Test
  void testRunDailyMissionRotationException() {
    doThrow(new RuntimeException("DB error")).when(dailyMissionService).rotateDailyMissions();

    // Should handle exception internally and not throw
    dailyMissionScheduler.runDailyMissionRotation();

    verify(dailyMissionService, times(1)).rotateDailyMissions();
  }
}
