package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserDailyMissionProgressServiceTests {

  @Mock
  private UserDailyMissionRepository userDailyMissionRepository;

  private UserDailyMissionProgressService service;
  private UUID userId;
  private DailyMission mission;
  private UserDailyMission userDailyMission;

  @BeforeEach
  void setUp() {
    service = new UserDailyMissionProgressService(userDailyMissionRepository);
    userId = UUID.randomUUID();

    mission = new DailyMission();
    mission.setId(7L);

    userDailyMission = new UserDailyMission();
    userDailyMission.setId(10L);
    userDailyMission.setUserId(userId);
    userDailyMission.setDailyMission(mission);
    userDailyMission.setCurrentProgress(2);
    userDailyMission.setCompleted(false);
  }

  @Test
  void findUserDailyMissionReturnsFirstProgressWhenDuplicatesExist() {
    UserDailyMission duplicate = new UserDailyMission();
    duplicate.setId(11L);

    when(userDailyMissionRepository.findAllByUserIdAndDailyMissionIdOrderByIdAsc(userId, 7L))
        .thenReturn(List.of(userDailyMission, duplicate));

    Optional<UserDailyMission> result = service.findUserDailyMission(userId, 7L);

    assertTrue(result.isPresent());
    assertSame(userDailyMission, result.get());
  }

  @Test
  void getOrCreateUserDailyMissionReturnsExistingProgress() {
    when(userDailyMissionRepository.findAllByUserIdAndDailyMissionIdOrderByIdAsc(userId, 7L))
        .thenReturn(List.of(userDailyMission));

    UserDailyMission result = service.getOrCreateUserDailyMission(userId, mission);

    assertSame(userDailyMission, result);
  }

  @Test
  void getOrCreateUserDailyMissionCreatesNewProgressWhenMissing() {
    when(userDailyMissionRepository.findAllByUserIdAndDailyMissionIdOrderByIdAsc(userId, 7L))
        .thenReturn(List.of(), List.of(userDailyMission));

    UserDailyMission result = service.getOrCreateUserDailyMission(userId, mission);

    verify(userDailyMissionRepository).insertIfMissing(userId, 7L);
    assertSame(userDailyMission, result);
  }

  @Test
  void getOrCreateUserDailyMissionRecoversWhenConcurrentInsertAlreadyCreatedProgress() {
    when(userDailyMissionRepository.findAllByUserIdAndDailyMissionIdOrderByIdAsc(userId, 7L))
        .thenReturn(List.of(), List.of(userDailyMission));

    UserDailyMission result = service.getOrCreateUserDailyMission(userId, mission);

    verify(userDailyMissionRepository).insertIfMissing(userId, 7L);
    assertSame(userDailyMission, result);
  }

  @Test
  void getOrCreateUserDailyMissionThrowsWhenInsertCannotBeReloaded() {
    when(userDailyMissionRepository.findAllByUserIdAndDailyMissionIdOrderByIdAsc(userId, 7L))
        .thenReturn(List.of(), List.of());

    assertThrows(IllegalStateException.class,
        () -> service.getOrCreateUserDailyMission(userId, mission));
  }
}
