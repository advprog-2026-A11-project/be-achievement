CREATE TABLE achievements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    milestone INTEGER NOT NULL,
    milestone_type VARCHAR(50),
    icon_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE daily_mission (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    target_milestone INTEGER NOT NULL,
    reward_points INTEGER,
    mission_type VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    active_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE user_achievements (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    achievement_id BIGINT NOT NULL REFERENCES achievements(id),
    unlocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_showcased BOOLEAN DEFAULT FALSE,
    UNIQUE(user_id, achievement_id)
);

CREATE TABLE user_daily_mission (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    mission_id BIGINT NOT NULL REFERENCES daily_mission(id),
    current_progress INTEGER DEFAULT 0,
    is_completed BOOLEAN DEFAULT FALSE
);

CREATE TABLE user_quiz_count (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    quiz_count INTEGER DEFAULT 0,
    last_processed_event_id VARCHAR(255)
);
