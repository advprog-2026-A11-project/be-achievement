package id.ac.ui.cs.advprog.beachievement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.beachievement.model.ClanPromotedEvent;
import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.service.AchievementListenerService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EventController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private AchievementListenerService achievementListenerService;

  @Test
  void handleQuizCompletedEventReturnsSuccess() throws Exception {
    QuizCompletedEvent event = validEvent();
    event.setEventId("event-1");

    mockMvc.perform(post("/api/events/quiz-completed")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").value("SUCCESS"));

    verify(achievementListenerService).processQuizCompleted(any(QuizCompletedEvent.class));
  }

  @Test
  void handleQuizCompletedEventGeneratesEventIdWhenMissing() throws Exception {
    QuizCompletedEvent event = validEvent();

    mockMvc.perform(post("/api/events/quiz-completed")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));

    ArgumentCaptor<QuizCompletedEvent> captor = ArgumentCaptor.forClass(QuizCompletedEvent.class);
    verify(achievementListenerService).processQuizCompleted(captor.capture());
    UUID.fromString(captor.getValue().getEventId());
  }

  @Test
  void handleQuizCompletedEventReturnsBadRequestWhenUserIdIsNull() throws Exception {
    QuizCompletedEvent event = validEvent();
    event.setUserId(null);

    mockMvc.perform(post("/api/events/quiz-completed")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("User ID must not be null"));

    verifyNoInteractions(achievementListenerService);
  }

  @Test
  void handleQuizCompletedEventReturnsInternalServerErrorWhenServiceThrows() throws Exception {
    QuizCompletedEvent event = validEvent();
    doThrow(new RuntimeException("listener failed"))
        .when(achievementListenerService).processQuizCompleted(any(QuizCompletedEvent.class));

    mockMvc.perform(post("/api/events/quiz-completed")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message")
            .value("Failed to process quiz completed event: listener failed"));
  }

  @Test
  void handleClanPromotedEventReturnsSuccess() throws Exception {
    ClanPromotedEvent event = validClanEvent();
    event.setEventId("event-clan-1");

    mockMvc.perform(post("/api/events/clan-promoted")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").value("SUCCESS"));

    verify(achievementListenerService).processClanPromoted(any(ClanPromotedEvent.class));
  }

  @Test
  void handleClanPromotedEventGeneratesEventIdWhenMissing() throws Exception {
    ClanPromotedEvent event = validClanEvent();

    mockMvc.perform(post("/api/events/clan-promoted")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));

    ArgumentCaptor<ClanPromotedEvent> captor = ArgumentCaptor.forClass(ClanPromotedEvent.class);
    verify(achievementListenerService).processClanPromoted(captor.capture());
    UUID.fromString(captor.getValue().getEventId());
  }

  @Test
  void handleClanPromotedEventAcknowledgesWhenUserIdsIsNull() throws Exception {
    ClanPromotedEvent event = validClanEvent();
    event.setUserIds(null);

    mockMvc.perform(post("/api/events/clan-promoted")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").value("NO_USERS"));

    verifyNoInteractions(achievementListenerService);
  }

  @Test
  void handleClanPromotedEventAcknowledgesWhenUserIdsIsEmpty() throws Exception {
    ClanPromotedEvent event = validClanEvent();
    event.setUserIds(List.of());

    mockMvc.perform(post("/api/events/clan-promoted")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").value("NO_USERS"));

    verifyNoInteractions(achievementListenerService);
  }

  @Test
  void handleClanPromotedEventIgnoresNonDiamondTier() throws Exception {
    ClanPromotedEvent event = validClanEvent();
    event.setTier("Platinum");

    mockMvc.perform(post("/api/events/clan-promoted")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").value("IGNORED"));

    verifyNoInteractions(achievementListenerService);
  }

  @Test
  void handleClanPromotedEventReturnsInternalServerErrorWhenServiceThrows() throws Exception {
    ClanPromotedEvent event = validClanEvent();
    doThrow(new RuntimeException("listener failed"))
        .when(achievementListenerService).processClanPromoted(any(ClanPromotedEvent.class));

    mockMvc.perform(post("/api/events/clan-promoted")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message")
            .value("Failed to process clan promoted event: listener failed"));
  }

  private QuizCompletedEvent validEvent() {
    QuizCompletedEvent event = new QuizCompletedEvent();
    event.setUserId(UUID.randomUUID());
    event.setScore(100);
    event.setAccuracy(95.0);
    return event;
  }

  private ClanPromotedEvent validClanEvent() {
    ClanPromotedEvent event = new ClanPromotedEvent();
    event.setClanId("clan-1");
    event.setClanName("Alpha Clan");
    event.setTier("Diamond");
    event.setUserIds(List.of(UUID.randomUUID()));
    return event;
  }
}
