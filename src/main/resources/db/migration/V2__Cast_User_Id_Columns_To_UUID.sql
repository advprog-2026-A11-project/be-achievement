DO $$
DECLARE
  target_table text;
BEGIN
  FOREACH target_table IN ARRAY ARRAY[
    'user_achievements',
    'user_daily_mission',
    'user_quiz_count',
    'user_quiz_counts'
  ]
  LOOP
    IF EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'public'
        AND table_name = target_table
        AND column_name = 'user_id'
        AND data_type <> 'uuid'
    ) THEN
      EXECUTE format(
        'ALTER TABLE %I ALTER COLUMN user_id TYPE UUID USING user_id::uuid',
        target_table
      );
    END IF;
  END LOOP;
END $$;
