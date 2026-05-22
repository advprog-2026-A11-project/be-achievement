#!/usr/bin/env bash
set -euo pipefail

COMPOSE_FILE="profiling/docker-compose.profiling.yml"
DB_SERVICE="achievement-profiling-db"

docker compose -f "$COMPOSE_FILE" up -d "$DB_SERVICE"

echo "Waiting for profiling database..."
until docker compose -f "$COMPOSE_FILE" exec -T "$DB_SERVICE" pg_isready -U achievement -d achievement_profile >/dev/null 2>&1; do
  sleep 2
done

echo "Resetting profiling database schema..."
docker compose -f "$COMPOSE_FILE" exec -T "$DB_SERVICE" \
  psql -U achievement -d achievement_profile \
  -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"

echo "Profiling database reset. Start the app so Flyway recreates the schema, then seed again."
