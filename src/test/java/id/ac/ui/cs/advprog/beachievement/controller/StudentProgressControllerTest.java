package id.ac.ui.cs.advprog.beachievement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.beachievement.model.UserDailyMission;
import id.ac.ui.cs.advprog.beachievement.service.StudentProgressService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StudentProgressController.class)
@ActiveProfiles("test")
class StudentProgressControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentProgressService studentProgressService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testGetMissions() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDailyMission mission = new UserDailyMission();
    mission.setId(1L);
    mission.setUserId(userId);

    when(studentProgressService.getStudentMissions(userId)).thenReturn(Arrays.asList(mission));

    mockMvc.perform(get("/api/student-progress/" + userId + "/missions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  @Test
  void testUpdateProgress() throws Exception {
    UUID userId = UUID.randomUUID();
    final Long missionId = 10L;
    Integer progress = 5;

    Map<String, Integer> body = new HashMap<>();
    body.put("progress", progress);

    UserDailyMission updatedMission = new UserDailyMission();
    updatedMission.setId(1L);
    updatedMission.setUserId(userId);
    updatedMission.setCurrentProgress(progress);

    when(studentProgressService.updateProgress(eq(userId), eq(missionId), eq(progress)))
        .thenReturn(updatedMission);

    mockMvc.perform(put("/api/student-progress/" + userId + "/missions/" + missionId + "/progress")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(body)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currentProgress").value(5));
  }
}
