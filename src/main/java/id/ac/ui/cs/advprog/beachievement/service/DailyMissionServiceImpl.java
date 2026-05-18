package id.ac.ui.cs.advprog.beachievement.service;

import id.ac.ui.cs.advprog.beachievement.model.DailyMission;
import id.ac.ui.cs.advprog.beachievement.repository.DailyMissionRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DailyMissionServiceImpl implements DailyMissionService {
  private final DailyMissionRepository dailyMissionRepository;

  public DailyMissionServiceImpl(DailyMissionRepository dailyMissionRepository) {
    this.dailyMissionRepository = dailyMissionRepository;
  }

  @Override
  public DailyMission create(DailyMission mission) {
    return dailyMissionRepository.save(mission);
  }

  @Override
  public List<DailyMission> findAll() {
    return dailyMissionRepository.findAll();
  }

  @Override
  public DailyMission update(Long id, DailyMission updatedData) {
    return dailyMissionRepository.findById(id).map(mission -> {
      mission.setTitle(updatedData.getTitle());
      mission.setDescription(updatedData.getDescription());
      mission.setTargetMilestone(updatedData.getTargetMilestone());
      mission.setRewardPoints(updatedData.getRewardPoints());
      mission.setActiveDate(updatedData.getActiveDate());
      mission.setMissionType(updatedData.getMissionType());
      mission.setActive(updatedData.isActive());
      return dailyMissionRepository.save(mission);
    }).orElse(null);
  }

  @Override
  public void delete(Long id) {
    dailyMissionRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void rotateDailyMissions() {
    LocalDate today = LocalDate.now();
    LocalDate yesterday = today.minusDays(1);

    List<DailyMission> allMissions = dailyMissionRepository.findAll();

    if (allMissions.isEmpty()) {
      allMissions = seedDefaultDailyMissions();
    }

    for (DailyMission mission : allMissions) {
      if (yesterday.equals(mission.getActiveDate())) {
        mission.setActive(false);
        dailyMissionRepository.save(mission);
      }
    }

    List<DailyMission> mutableMissions = new ArrayList<>(allMissions);
    Collections.shuffle(mutableMissions);
    int count = Math.min(mutableMissions.size(), 3);
    for (int i = 0; i < count; i++) {
      DailyMission mission = mutableMissions.get(i);
      mission.setActiveDate(today);
      mission.setActive(true);
      dailyMissionRepository.save(mission);
    }
  }

  private List<DailyMission> seedDefaultDailyMissions() {
    List<DailyMission> defaults = new ArrayList<>();

    DailyMission dm1 = new DailyMission();
    dm1.setTitle("Membaca Berita Harian");
    dm1.setDescription("Selesaikan 3 bacaan kategori News & Media");
    dm1.setTargetMilestone(3);
    dm1.setRewardPoints(10);
    dm1.setMissionType("READ_NEWS");
    dm1.setActive(false);
    defaults.add(dailyMissionRepository.save(dm1));

    DailyMission dm2 = new DailyMission();
    dm2.setTitle("Kuis Sempurna");
    dm2.setDescription("Selesaikan 2 kuis dengan akurasi 100%");
    dm2.setTargetMilestone(2);
    dm2.setRewardPoints(15);
    dm2.setMissionType("QUIZ_ACCURACY");
    dm2.setActive(false);
    defaults.add(dailyMissionRepository.save(dm2));

    DailyMission dm3 = new DailyMission();
    dm3.setTitle("Pecinta Sastra");
    dm3.setDescription("Selesaikan 1 bacaan kategori Sastra/Fiksi");
    dm3.setTargetMilestone(1);
    dm3.setRewardPoints(8);
    dm3.setMissionType("READ_FICTION");
    dm3.setActive(false);
    defaults.add(dailyMissionRepository.save(dm3));

    DailyMission dm4 = new DailyMission();
    dm4.setTitle("Ambisi Literasi");
    dm4.setDescription("Selesaikan kuis apa saja sebanyak 5 kali");
    dm4.setTargetMilestone(5);
    dm4.setRewardPoints(20);
    dm4.setMissionType("QUIZ_COUNT");
    dm4.setActive(false);
    defaults.add(dailyMissionRepository.save(dm4));

    return defaults;
  }
}