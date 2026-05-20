package id.ac.ui.cs.advprog.beachievement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeAchievementApplication {
  public static void main(String[] args) {
    SpringApplication.run(BeAchievementApplication.class, args);
  }
}
