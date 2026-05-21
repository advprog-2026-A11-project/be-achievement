WITH ranked AS (
    SELECT
        id,
        ROW_NUMBER() OVER (
            PARTITION BY user_id, mission_id
            ORDER BY is_completed DESC, current_progress DESC, id ASC
        ) AS row_number
    FROM user_daily_mission
)
DELETE FROM user_daily_mission udm
USING ranked
WHERE udm.id = ranked.id
  AND ranked.row_number > 1;

ALTER TABLE user_daily_mission
    ADD CONSTRAINT uk_user_daily_mission_user_mission UNIQUE (user_id, mission_id);
