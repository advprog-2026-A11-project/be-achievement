DO $$
DECLARE
  primary_key_name text;
BEGIN
  IF to_regclass('public.user_quiz_counts') IS NOT NULL
      AND to_regclass('public.user_quiz_count') IS NULL THEN
    ALTER TABLE user_quiz_counts RENAME TO user_quiz_count;
  END IF;

  IF to_regclass('public.user_quiz_count') IS NULL THEN
    CREATE TABLE user_quiz_count (
      user_id UUID PRIMARY KEY,
      quiz_count INTEGER NOT NULL DEFAULT 0,
      last_processed_event_id VARCHAR(255)
    );
  ELSE
    IF EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'public'
        AND table_name = 'user_quiz_count'
        AND column_name = 'user_id'
        AND data_type <> 'uuid'
    ) THEN
      ALTER TABLE user_quiz_count
        ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
    END IF;

    ALTER TABLE user_quiz_count
      ALTER COLUMN user_id SET NOT NULL;

    ALTER TABLE user_quiz_count
      ALTER COLUMN quiz_count SET DEFAULT 0;

    UPDATE user_quiz_count
    SET quiz_count = 0
    WHERE quiz_count IS NULL;

    ALTER TABLE user_quiz_count
      ALTER COLUMN quiz_count SET NOT NULL;

    SELECT constraint_name
    INTO primary_key_name
    FROM information_schema.table_constraints
    WHERE table_schema = 'public'
      AND table_name = 'user_quiz_count'
      AND constraint_type = 'PRIMARY KEY'
    LIMIT 1;

    IF primary_key_name IS NOT NULL THEN
      EXECUTE format(
        'ALTER TABLE user_quiz_count DROP CONSTRAINT %I',
        primary_key_name
      );
    END IF;

    ALTER TABLE user_quiz_count
      ADD PRIMARY KEY (user_id);
  END IF;
END $$;
