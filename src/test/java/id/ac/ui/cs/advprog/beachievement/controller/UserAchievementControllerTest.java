package id.ac.ui.cs.advprog.beachievement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.beachievement.dto.ToggleFeaturedRequest;
import id.ac.ui.cs.advprog.beachievement.model.Achievement;
import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.service.UserAchievementService;
import java.util.Collections;
import java.util.List;
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

@WebMvcTest(UserAchievementController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UserAchievementControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserAchievementService userAchievementService;

  @Autowired
  private ObjectMapper objectMapper;

  private static final UUID OWNER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

  private static final UUID OTHER_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  private UserAchievement buildUserAchievement() {
    Achievement achievement = new Achievement();
    achievement.setId(1L);
    achievement.setTitle("First Blood");
    achievement.setMilestone(1);

    UserAchievement ua = new UserAchievement();
    ua.setId(1L);
    ua.setUserId(OWNER_ID);
    ua.setAchievement(achievement);
    ua.setShowcased(false);
    return ua;
  }

  @Test
  @WithMockUser(username = "11111111-1111-1111-1111-111111111111")
  void getAllAchievementsSuccessWhenOwnerRequests() throws Exception {
    when(userAchievementService.getUnlockedAchievements(OWNER_ID))
        .thenReturn(List.of(buildUserAchievement()));

    mockMvc.perform(get("/api/users/" + OWNER_ID + "/achievements"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(1));
  }

  @Test
  @WithMockUser(username = "11111111-1111-1111-1111-111111111111")
  void getAllAchievementsReturnsEmptyListWhenUserHasNoAchievements() throws Exception {
    when(userAchievementService.getUnlockedAchievements(OWNER_ID))
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/users/" + OWNER_ID + "/achievements"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(0));
  }

  @Test
  @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
  void getAllAchievementsForbiddenWhenOtherUserRequests() throws Exception {
    mockMvc.perform(get("/api/users/" + OWNER_ID + "/achievements"))
        .andExpect(status().isForbidden());

    verify(userAchievementService, never()).getUnlockedAchievements(any());
  }

  @Test
  void getAllAchievementsUnauthorizedWhenNoAuthentication() throws Exception {
    mockMvc.perform(get("/api/users/" + OWNER_ID + "/achievements"))
        .andExpect(status().isUnauthorized());

    verify(userAchievementService, never()).getUnlockedAchievements(any());
  }

  @Test
  @WithMockUser(username = "11111111-1111-1111-1111-111111111111")
  void getFeaturedAchievementsSuccessWhenOwnerRequests() throws Exception {
    UserAchievement ua = buildUserAchievement();
    ua.setShowcased(true);
    when(userAchievementService.getPublicAchievements(OWNER_ID)).thenReturn(List.of(ua));

    mockMvc.perform(get("/api/users/" + OWNER_ID + "/achievements/featured"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(1));
  }

  @Test
  @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
  void getFeaturedAchievementsForbiddenWhenOtherUserRequests() throws Exception {
    mockMvc.perform(get("/api/users/" + OWNER_ID + "/achievements/featured"))
        .andExpect(status().isForbidden());

    verify(userAchievementService, never()).getPublicAchievements(any());
  }

  @Test
  @WithMockUser(username = "11111111-1111-1111-1111-111111111111")
  void toggleFeaturedAchievementSuccessWhenOwnerRequests() throws Exception {
    ToggleFeaturedRequest request = new ToggleFeaturedRequest(true);
    doNothing().when(userAchievementService)
        .setFeaturedAchievement(eq(OWNER_ID), eq(5L), eq(true));

    mockMvc.perform(put("/api/users/" + OWNER_ID + "/achievements/5/featured")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Successfully updated featured status"));
  }

  @Test
  @WithMockUser(username = "11111111-1111-1111-1111-111111111111")
  void toggleFeaturedAchievementNotFoundThrowsException() throws Exception {
    ToggleFeaturedRequest request = new ToggleFeaturedRequest(true);
    doThrow(new RuntimeException("not found")).when(userAchievementService)
        .setFeaturedAchievement(eq(OWNER_ID), eq(999L), eq(true));

    mockMvc.perform(put("/api/users/" + OWNER_ID + "/achievements/999/featured")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "22222222-2222-2222-2222-222222222222")
  void toggleFeaturedAchievementForbiddenWhenOtherUserRequests() throws Exception {
    ToggleFeaturedRequest request = new ToggleFeaturedRequest(false);

    mockMvc.perform(put("/api/users/" + OWNER_ID + "/achievements/5/featured")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());

    verify(userAchievementService, never())
        .setFeaturedAchievement(any(), any(), any(Boolean.class));
  }
}
