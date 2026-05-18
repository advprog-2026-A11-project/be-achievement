package id.ac.ui.cs.advprog.beachievement.controller;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StudentAchievementController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class StudentAchievementControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserAchievementService userAchievementService;

  private static final UUID USER_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

  private UserAchievement buildAchievement(boolean showcased) {
    Achievement achievement = new Achievement();
    achievement.setId(1L);
    achievement.setTitle("Scholar");
    achievement.setMilestone(5);

    UserAchievement ua = new UserAchievement();
    ua.setId(10L);
    ua.setUserId(USER_ID);
    ua.setAchievement(achievement);
    ua.setShowcased(showcased);
    return ua;
  }

  @Test
  @WithMockUser(username = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
  void getMyAchievementsSuccessWhenAuthenticated() throws Exception {
    when(userAchievementService.getUnlockedAchievements(USER_ID))
        .thenReturn(List.of(buildAchievement(false)));

    mockMvc.perform(get("/api/achievements/me"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(1));
  }

  @Test
  @WithMockUser(username = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
  void getMyAchievementsReturnsEmptyListWhenNoAchievements() throws Exception {
    when(userAchievementService.getUnlockedAchievements(USER_ID))
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/achievements/me"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(0));
  }

  @Test
  void getMyAchievementsUnauthorizedWhenNoAuthentication() throws Exception {
    mockMvc.perform(get("/api/achievements/me"))
        .andExpect(status().isUnauthorized());

    verify(userAchievementService, never()).getUnlockedAchievements(USER_ID);
  }

  @Test
  void getPublicAchievementsSuccessNoAuthRequired() throws Exception {
    when(userAchievementService.getPublicAchievements(USER_ID))
        .thenReturn(List.of(buildAchievement(true)));

    mockMvc.perform(get("/api/achievements/" + USER_ID + "/public"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.length()").value(1));
  }

  @Test
  void getPublicAchievementsReturnsEmptyWhenNoneShowcased() throws Exception {
    when(userAchievementService.getPublicAchievements(USER_ID))
        .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/achievements/" + USER_ID + "/public"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(0));
  }

  @Test
  @WithMockUser(username = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
  void setFeaturedAchievementSuccessWhenAuthenticated() throws Exception {
    mockMvc.perform(put("/api/achievements/featured/1")
        .param("showcased", "true"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Featured achievement status updated"));

    verify(userAchievementService).setFeaturedAchievement(USER_ID, 1L, true);
  }

  @Test
  @WithMockUser(username = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
  void setFeaturedAchievementShowcaseFalse() throws Exception {
    mockMvc.perform(put("/api/achievements/featured/2")
        .param("showcased", "false"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));

    verify(userAchievementService).setFeaturedAchievement(USER_ID, 2L, false);
  }

  @Test
  void setFeaturedAchievementUnauthorizedWhenNoAuthentication() throws Exception {
    mockMvc.perform(put("/api/achievements/featured/1")
        .param("showcased", "true"))
        .andExpect(status().isUnauthorized());
  }
}
