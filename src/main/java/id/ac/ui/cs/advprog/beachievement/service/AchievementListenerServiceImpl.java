package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.QuizCompletedEvent;
import org.springframework.stereotype.Service;

@Service
public class AchievementListenerServiceImpl implements AchievementListenerService {

    @Override
    public void processQuizCompleted(QuizCompletedEvent event) {
        // TODO: Implement your business logic here
        System.out.println("Successfully received quiz completed event for user: " + event.getUserId());
        System.out.println("Score: " + event.getScore());
        System.out.println("Accuracy: " + event.getAccuracy());
    }
}
