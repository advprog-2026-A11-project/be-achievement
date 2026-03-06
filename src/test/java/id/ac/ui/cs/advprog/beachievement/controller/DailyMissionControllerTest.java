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
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DailyMissionController.class)
@ActiveProfiles("test")
class DailyMissionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DailyMissionService dailyMissionService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testCreateMission() throws Exception {
    DailyMissionRequest request = new DailyMissionRequest();
    request.setTitle("Misi 1");

    DailyMission saved = new DailyMission();
    saved.setId(1L);
    saved.setTitle("Misi 1");

    when(dailyMissionService.create(any(DailyMission.class))).thenReturn(saved);

    mockMvc.perform(post("/api/admin/daily-missions") // Path sudah sinkron
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Misi 1"));
  }

  @Test
  void testDeleteMission() throws Exception {
    doNothing().when(dailyMissionService).delete(1L);

    mockMvc.perform(delete("/api/admin/daily-missions/1")) // Path sudah sinkron
        .andExpect(status().isOk()) // Sesuai return ResponseEntity.ok() di Controller
        .andExpect(content().string("Daily Mission deleted successfully!"));
  }
}