# Achievement Profiling Dataset

This folder provides an isolated profiling environment for `be-achievement`.

It is intended for performance evidence such as:
- before and after code profiling
- APDEX or latency comparison
- database query observation
- endpoint response-time comparison

## Files

- `docker-compose.profiling.yml`: runs the Achievement backend and a separate PostgreSQL database.
- `seed_profiling.sql`: inserts a large Achievement-specific dataset.
- `seed_profiling_data.sh`: starts the database and applies the seed data.
- `reset_profiling_db.sh`: drops and recreates the profiling database schema.

## Start Profiling Stack

```bash
docker compose -f profiling/docker-compose.profiling.yml up --build -d
```

Default ports:
- App: `18082`
- DB: `55434`

## Seed Data

Run the app once so Flyway creates the schema, then seed the profiling data:

```bash
bash profiling/seed_profiling_data.sh
```

## Reset Data

```bash
bash profiling/reset_profiling_db.sh
```

After reset:
1. Start the backend again so Flyway recreates tables.
2. Run `bash profiling/seed_profiling_data.sh`.

## Suggested Endpoints to Compare

Use the seeded user ID:

```text
00000000-0000-0000-0000-000000000001
```

Suggested requests:

```bash
curl http://localhost:18082/api/student-progress/00000000-0000-0000-0000-000000000001/missions
curl http://localhost:18082/api/student-progress/00000000-0000-0000-0000-000000000001/score
curl http://localhost:18082/api/achievements/00000000-0000-0000-0000-000000000001/completed
```

For event processing:

```bash
curl -X POST http://localhost:18082/api/events/quiz-completed \
  -H "Content-Type: application/json" \
  -d '{
    "eventId": "profiling-event-001",
    "userId": "00000000-0000-0000-0000-000000000001",
    "readingId": "profiling-reading-001",
    "category": "NEWS",
    "difficultyLevel": "BEGINNER",
    "score": 100,
    "accuracy": 100.0
  }'
```

## What This Dataset Stresses

- Many achievements per user profile.
- Many daily mission progress rows across users.
- Mission matching by `missionType` and `activeDate`.
- Score calculation from completed and claimed missions.
- Event listener updates for quiz completion.
