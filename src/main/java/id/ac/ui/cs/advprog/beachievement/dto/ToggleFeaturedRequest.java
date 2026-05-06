package id.ac.ui.cs.advprog.beachievement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToggleFeaturedRequest {
  private boolean featured;
}
