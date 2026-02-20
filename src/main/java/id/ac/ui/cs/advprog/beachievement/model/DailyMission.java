package id.ac.ui.cs.advprog.beachievement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DailyMission {
  @Id
  private String id;
  private String title;
  private int requirement; //ex: "read text: 3"
  private boolean isActive;
}
