INSERT INTO achievements (id, title, description, milestone, milestone_type, icon_url, created_at)
SELECT
  series_id,
  'Profiling Achievement ' || series_id,
  'Generated achievement for profiling dataset',
  1 + (series_id % 25),
  CASE series_id % 5
    WHEN 0 THEN 'QUIZ_COUNT'
    WHEN 1 THEN 'READ_NEWS'
    WHEN 2 THEN 'READ_FICTION'
    WHEN 3 THEN 'QUIZ_ACCURACY'
    ELSE 'CLAN_DIAMOND'
  END,
  'https://example.com/achievement-' || series_id || '.png',
  CURRENT_TIMESTAMP
FROM generate_series(1, 1000) AS series_id
ON CONFLICT (id) DO NOTHING;

INSERT INTO daily_mission (
  id, title, description, target_milestone, reward_points, mission_type,
  is_active, active_date, created_at, updated_at
)
SELECT
  series_id,
  'Profiling Daily Mission ' || series_id,
  'Generated daily mission for profiling dataset',
  1 + (series_id % 10),
  5 + (series_id % 50),
  CASE series_id % 4
    WHEN 0 THEN 'QUIZ_COUNT'
    WHEN 1 THEN 'READ_NEWS'
    WHEN 2 THEN 'READ_FICTION'
    ELSE 'QUIZ_ACCURACY'
  END,
  TRUE,
  CURRENT_DATE - (series_id % 30),
  CURRENT_TIMESTAMP,
  CURRENT_TIMESTAMP
FROM generate_series(1, 600) AS series_id
ON CONFLICT (id) DO NOTHING;

INSERT INTO user_quiz_count (user_id, quiz_count, last_processed_event_id)
SELECT
  ('00000000-0000-0000-0000-' || lpad(series_id::text, 12, '0'))::uuid,
  10 + (series_id % 500),
  'profiling-seed-' || series_id
FROM generate_series(1, 5000) AS series_id
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO user_achievements (
  user_id, achievement_id, unlocked_at, is_showcased
)
SELECT
  ('00000000-0000-0000-0000-' || lpad(user_id::text, 12, '0'))::uuid,
  achievement_id,
  CURRENT_TIMESTAMP - ((achievement_id % 90) || ' days')::interval,
  achievement_id <= 3
FROM generate_series(1, 5000) AS user_id
CROSS JOIN generate_series(1, 25) AS achievement_id
ON CONFLICT (user_id, achievement_id) DO NOTHING;

INSERT INTO user_daily_mission (
  user_id, mission_id, current_progress, is_completed, reward_claimed
)
SELECT
  ('00000000-0000-0000-0000-' || lpad(user_id::text, 12, '0'))::uuid,
  mission_id,
  mission_id % 10,
  (mission_id % 3 = 0),
  (mission_id % 6 = 0)
FROM generate_series(1, 5000) AS user_id
CROSS JOIN generate_series(1, 30) AS mission_id
ON CONFLICT (user_id, mission_id) DO NOTHING;

SELECT setval('achievements_id_seq', (SELECT MAX(id) FROM achievements));
SELECT setval('daily_mission_id_seq', (SELECT MAX(id) FROM daily_mission));
