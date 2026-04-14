package id.ac.ui.cs.advprog.beachievement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import id.ac.ui.cs.advprog.beachievement.repository.UserDailyMissionRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AchievementListenerServiceImplTests {

    @Mock
    private DailyMissionRepository dailyMissionRepository;

    @Mock
    private UserDailyMissionRepository userDailyMissionRepository;

    @InjectMocks
    private AchievementListenerServiceImpl achievementListenerService;

    private UUID userId;
    private QuizCompletedEvent event;
    private DailyMission mission;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        event = new QuizCompletedEvent();
        event.setUserId(userId);
        event.setScore(100);
        event.setAccuracy(100.0);

        mission = new DailyMission();
        mission.setId(1L);
        mission.setTitle("Test Mission");
        mission.setTargetMilestone(5);
        mission.setActiveDate(LocalDate.now());
    }

    @Test
    void testProcessQuizCompletedWithNoActiveMissions() {
        when(dailyMissionRepository.findByActiveDate(any(LocalDate.class)))
            .thenReturn(Collections.emptyList());

        achievementListenerService.processQuizCompleted(event);

        verify(userDailyMissionRepository, never()).save(any());
    }

    @Test
    void testProcessQuizCompletedCreatesNewMapping() {
        when(dailyMissionRepository.findByActiveDate(any(LocalDate.class)))
            .thenReturn(Arrays.asList(mission));
        when(userDailyMissionRepository.findByUserIdAndDailyMissionId(userId, mission.getId()))
            .thenReturn(Optional.empty());

        achievementListenerService.processQuizCompleted(event);

        ArgumentCaptor<UserDailyMission> captor = ArgumentCaptor.forClass(UserDailyMission.class);
        verify(userDailyMissionRepository).save(captor.capture());

        UserDailyMission saved = captor.getValue();
        assertEquals(userId, saved.getUserId());
        assertEquals(mission, saved.getDailyMission());
        assertEquals(1, saved.getCurrentProgress());
        assertFalse(saved.isCompleted());
    }

    @Test
    void testProcessQuizCompletedIncrementsExisting() {
        UserDailyMission existing = new UserDailyMission();
        existing.setUserId(userId);
        existing.setDailyMission(mission);
        existing.setCurrentProgress(2);
        existing.setCompleted(false);

        when(dailyMissionRepository.findByActiveDate(any(LocalDate.class)))
            .thenReturn(Arrays.asList(mission));
        when(userDailyMissionRepository.findByUserIdAndDailyMissionId(userId, mission.getId()))
            .thenReturn(Optional.of(existing));

        achievementListenerService.processQuizCompleted(event);

        verify(userDailyMissionRepository).save(existing);
        assertEquals(3, existing.getCurrentProgress());
    }

    @Test
    void testProcessQuizCompletedSetsCompleted() {
        UserDailyMission existing = new UserDailyMission();
        existing.setUserId(userId);
        existing.setDailyMission(mission);
        existing.setCurrentProgress(4); // target is 5
        existing.setCompleted(false);

        when(dailyMissionRepository.findByActiveDate(any(LocalDate.class)))
            .thenReturn(Arrays.asList(mission));
        when(userDailyMissionRepository.findByUserIdAndDailyMissionId(userId, mission.getId()))
            .thenReturn(Optional.of(existing));

        achievementListenerService.processQuizCompleted(event);

        assertTrue(existing.isCompleted());
        assertEquals(5, existing.getCurrentProgress());
        verify(userDailyMissionRepository).save(existing);
    }

    @Test
    void testProcessQuizCompletedSkipsIfAlreadyCompleted() {
        UserDailyMission completed = new UserDailyMission();
        completed.setUserId(userId);
        completed.setDailyMission(mission);
        completed.setCurrentProgress(5);
        completed.setCompleted(true);

        when(dailyMissionRepository.findByActiveDate(any(LocalDate.class)))
            .thenReturn(Arrays.asList(mission));
        when(userDailyMissionRepository.findByUserIdAndDailyMissionId(userId, mission.getId()))
            .thenReturn(Optional.of(completed));

        achievementListenerService.processQuizCompleted(event);

        verify(userDailyMissionRepository, never()).save(any());
    }
}
