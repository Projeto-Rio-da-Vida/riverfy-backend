CREATE TABLE events (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description TEXT NOT NULL,
                        date TIMESTAMP NOT NULL,
                        event_status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
                        active BOOLEAN NOT NULL DEFAULT TRUE,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP
);

CREATE TABLE event_participants (
                                    event_id BIGINT NOT NULL,
                                    user_id BIGINT NOT NULL,
                                    PRIMARY KEY (event_id, user_id),
                                    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
                                    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);