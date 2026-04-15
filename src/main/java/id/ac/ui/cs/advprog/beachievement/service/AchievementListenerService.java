package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;

public interface AchievementListenerService {
  void processQuizCompleted(QuizCompletedEvent event);
}
