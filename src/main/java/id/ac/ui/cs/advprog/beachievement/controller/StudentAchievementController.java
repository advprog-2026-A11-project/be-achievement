package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.service.UserAchievementService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievements")
public class StudentAchievementController {

    private final UserAchievementService userAchievementService;

    public StudentAchievementController(UserAchievementService userAchievementService) {
        this.userAchievementService = userAchievementService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserAchievement>> getMyAchievements() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(auth.getName());

        return ResponseEntity.ok(userAchievementService.getUnlockedAchievements(userId));
    }

    @GetMapping("/{userId}/public")
    public ResponseEntity<List<UserAchievement>> getPublicAchievements(@PathVariable UUID userId) {
        return ResponseEntity.ok(userAchievementService.getPublicAchievements(userId));
    }
}