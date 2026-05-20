package id.ac.ui.cs.advprog.beachievement.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DailyMissionTest {

  @Test
  void isActiveReturnsTrueOnlyWhenFieldIsTrue() {
    DailyMission mission = new DailyMission();

    mission.setActive(true);
    assertTrue(mission.isActive());

    mission.setActive(false);
    assertFalse(mission.isActive());

    mission.setActive(null);
    assertFalse(mission.isActive());
  }

  @Test
  void gettersAndSettersExposeMissionFields() {
    DailyMission mission = new DailyMission();
    LocalDate activeDate = LocalDate.now();
    LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
    LocalDateTime updatedAt = LocalDateTime.now();

    mission.setId(1L);
    mission.setTitle("Complete quiz");
    mission.setDescription("Finish one quiz");
    mission.setTargetMilestone(1);
    mission.setRewardPoints(25);
    mission.setMissionType("QUIZ");
    mission.setActiveDate(activeDate);
    mission.setCreatedAt(createdAt);
    mission.setUpdatedAt(updatedAt);

    assertEquals(1L, mission.getId());
    assertEquals("Complete quiz", mission.getTitle());
    assertEquals("Finish one quiz", mission.getDescription());
    assertEquals(1, mission.getTargetMilestone());
    assertEquals(25, mission.getRewardPoints());
    assertEquals("QUIZ", mission.getMissionType());
    assertEquals(activeDate, mission.getActiveDate());
    assertEquals(createdAt, mission.getCreatedAt());
    assertEquals(updatedAt, mission.getUpdatedAt());
  }

  @Test
  void defaultConstructorInitializesActiveToTrueAndOtherFieldsNull() {
    DailyMission mission = new DailyMission();

    assertTrue(mission.isActive());
    assertNull(mission.getId());
    assertNull(mission.getTitle());
  }
}
