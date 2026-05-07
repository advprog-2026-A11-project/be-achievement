package id.ac.ui.cs.advprog.beachievement.controller;

import id.ac.ui.cs.advprog.beachievement.dto.ApiResponse;
import id.ac.ui.cs.advprog.beachievement.dto.ToggleFeaturedRequest;
import id.ac.ui.cs.advprog.beachievement.dto.UserAchievementResponse;
import id.ac.ui.cs.advprog.beachievement.model.UserAchievement;
import id.ac.ui.cs.advprog.beachievement.service.UserAchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users/{userId}/achievements")
@Tag(name = "User Achievements", description = "Endpoints for managing user achievements")
public class UserAchievementController {

  private final UserAchievementService userAchievementService;

  public UserAchievementController(UserAchievementService userAchievementService) {
    this.userAchievementService = userAchievementService;
  }

  /**
   * Helper method to validate if the authenticated user matches the path variable.
   * Extracts user identity from the SecurityContext populated by JwtAuthFilter.
   */
  private void validateUserAccess(UUID pathUserId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getPrincipal() == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
    }
    
    // In JwtAuthFilter, principal is set as the userId string
    String tokenUserIdStr = (String) authentication.getPrincipal();
    UUID tokenUserId;
    try {
      tokenUserId = UUID.fromString(tokenUserIdStr);
    } catch (IllegalArgumentException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token user ID format");
    }

    if (!tokenUserId.equals(pathUserId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: User ID mismatch");
    }
  }

  @GetMapping
  @Operation(summary = "Get all achievements of a user")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "Successfully retrieved list of achievements"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "403", description = "Access denied")
  })
  public ResponseEntity<ApiResponse<List<UserAchievementResponse>>> getAllAchievements(
      @PathVariable UUID userId) {
    validateUserAccess(userId);

    List<UserAchievement> achievements = userAchievementService.getUnlockedAchievements(userId);
    List<UserAchievementResponse> responseList = achievements.stream()
        .map(UserAchievementResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        ApiResponse.success("Successfully fetched achievements", responseList));
  }

  @GetMapping("/featured")
  @Operation(summary = "Get featured (showcased) achievements of a user")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "Successfully retrieved featured achievements"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "403", description = "Access denied")
  })
  public ResponseEntity<ApiResponse<List<UserAchievementResponse>>> getFeaturedAchievements(
      @PathVariable UUID userId) {
    // Note: Featured achievements might be public, but following strict rules to match requirements
    // if it needs to be public, we can remove validateUserAccess. 
    // The prompt says: "Compare JWT userId with {userId} path variable". So we keep it.
    validateUserAccess(userId);

    List<UserAchievement> achievements = userAchievementService.getPublicAchievements(userId);
    List<UserAchievementResponse> responseList = achievements.stream()
        .map(UserAchievementResponse::fromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        ApiResponse.success("Successfully fetched featured achievements", responseList));
  }

  @PutMapping("/{achievementId}/featured")
  @Operation(summary = "Toggle featured status of an achievement")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "200", description = "Successfully toggled featured status"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(
          responseCode = "403", description = "Access denied")
  })
  public ResponseEntity<ApiResponse<Void>> toggleFeaturedAchievement(
      @PathVariable UUID userId,
      @PathVariable Long achievementId,
      @RequestBody ToggleFeaturedRequest request) {
    
    validateUserAccess(userId);

    try {
      userAchievementService.setFeaturedAchievement(userId, achievementId, request.isFeatured());
      return ResponseEntity.ok(ApiResponse.success("Successfully updated featured status", null));
    } catch (Exception e) {
      // In a more robust system, we would have specific exceptions like EntityNotFoundException
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Achievement not found for the user");
    }
  }
}
