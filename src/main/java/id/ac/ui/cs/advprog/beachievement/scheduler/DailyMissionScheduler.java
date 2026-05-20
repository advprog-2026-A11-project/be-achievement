package id.ac.ui.cs.advprog.beachievement.scheduler;

import id.ac.ui.cs.advprog.beachievement.service.DailyMissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DailyMissionScheduler {

  private final DailyMissionService dailyMissionService;

  public DailyMissionScheduler(DailyMissionService dailyMissionService) {
    this.dailyMissionService = dailyMissionService;
  }

  // Runs every day at midnight (00:00:00)
  @Scheduled(cron = "0 0 0 * * *")
  public void runDailyMissionRotation() {
    log.info("Starting scheduled daily mission rotation...");
    try {
      dailyMissionService.rotateDailyMissions();
      log.info("Successfully completed daily mission rotation.");
    } catch (Exception e) {
      log.error("Failed to rotate daily missions", e);
    }
  }
}
