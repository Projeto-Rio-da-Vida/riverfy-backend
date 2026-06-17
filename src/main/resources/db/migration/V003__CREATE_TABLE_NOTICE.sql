CREATE TABLE notices (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR NOT NULL,
                         description TEXT NOT NULL,
                         date DATE NOT NULL,
                         active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP
);

CREATE INDEX idx_notices_search ON notices (active, name);