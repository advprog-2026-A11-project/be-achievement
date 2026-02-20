package id.ac.ui.cs.advprog.beachievement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Achievement {
  @Id
  private String id;
  private String name;
  private int milestone;
  private String description;
}