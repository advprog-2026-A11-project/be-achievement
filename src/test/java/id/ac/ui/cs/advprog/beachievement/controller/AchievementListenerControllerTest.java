package id.ac.ui.cs.advprog.beachievement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import id.ac.ui.cs.advprog.beachievement.service.AchievementListenerService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AchievementListenerController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class AchievementListenerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AchievementListenerService achievementListenerService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "STUDENT")
  void testReceiveQuizCompletedEvent() throws Exception {
    QuizCompletedEvent event = new QuizCompletedEvent();
    event.setUserId(UUID.randomUUID());
    event.setScore(90);
    event.setAccuracy(95.0);

    doNothing().when(achievementListenerService)
        .processQuizCompleted(any(QuizCompletedEvent.class));

    mockMvc.perform(post("/api/events/quiz-completed")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(event)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Quiz completed event received successfully"));

    verify(achievementListenerService, times(1))
        .processQuizCompleted(any(QuizCompletedEvent.class));
  }
}