package id.ac.ui.cs.advprog.beachievement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Table(name = "achievements")
@NoArgsConstructor @AllArgsConstructor
public class Achievement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;
  private Integer milestone;
}