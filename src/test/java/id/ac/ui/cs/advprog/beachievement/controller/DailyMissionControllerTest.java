package id.ac.ui.cs.advprog.beachievement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.model.DailyMissionRequest;
import id.ac.ui.cs.advprog.beachievement.service.DailyMissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DailyMissionController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class DailyMissionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DailyMissionService dailyMissionService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "ADMIN")
  void testCreateMission() throws Exception {
    DailyMissionRequest request = new DailyMissionRequest();
    request.setTitle("Misi 1");

    DailyMission saved = new DailyMission();
    saved.setId(1L);
    saved.setTitle("Misi 1");

    when(dailyMissionService.create(any(DailyMission.class))).thenReturn(saved);

    mockMvc.perform(post("/api/admin/daily-missions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Misi 1"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testDeleteMission() throws Exception {
    doNothing().when(dailyMissionService).delete(1L);

    mockMvc.perform(delete("/api/admin/daily-missions/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("Daily Mission deleted successfully!"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testUpdateMissionNotFound() throws Exception {
    when(dailyMissionService.update(eq(999L), any(DailyMission.class))).thenReturn(null);

    DailyMissionRequest request = new DailyMissionRequest();
    request.setTitle("Ghost Mission");

    mockMvc.perform(put("/api/admin/daily-missions/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }
}