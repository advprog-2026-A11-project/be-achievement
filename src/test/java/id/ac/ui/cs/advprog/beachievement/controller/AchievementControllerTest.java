package id.ac.ui.cs.advprog.beachievement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.AchievementRequest;
import id.ac.ui.cs.advprog.beachievement.service.AchievementService;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AchievementController.class)
@ActiveProfiles("test")
class AchievementControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AchievementService achievementService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testCreateAchievement() throws Exception {
    AchievementRequest request = new AchievementRequest();
    request.setTitle("Master Reader");
    request.setDescription("Read 100 books");
    request.setMilestone(100);

    Achievement saved = new Achievement();
    saved.setId(1L);
    saved.setTitle("Master Reader");

    when(achievementService.create(any(Achievement.class))).thenReturn(saved);

    mockMvc.perform(post("/api/admin/achievements")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Master Reader"));
  }

  @Test
  void testGetAllAchievements() throws Exception {
    when(achievementService.findAll()).thenReturn(Arrays.asList(new Achievement()));

    mockMvc.perform(get("/api/admin/achievements"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1));
  }

  @Test
  void testDeleteAchievement() throws Exception {
    doNothing().when(achievementService).delete(1L);

    mockMvc.perform(delete("/api/admin/achievements/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("Achievement deleted successfully!"));

    verify(achievementService, times(1)).delete(1L);
  }
}