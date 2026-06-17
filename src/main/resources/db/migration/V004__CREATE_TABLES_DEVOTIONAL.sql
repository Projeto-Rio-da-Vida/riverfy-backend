CREATE TABLE devotionals (
                             id BIGSERIAL PRIMARY KEY,
                             title VARCHAR NOT NULL,
                             status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
                             active BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP
);

CREATE TABLE devotional_pages (
                                  id BIGSERIAL PRIMARY KEY,
                                  devotional_id BIGINT NOT NULL,
                                  title VARCHAR,
                                  text TEXT NOT NULL,
                                  page_number INT NOT NULL,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP,
                                  CONSTRAINT fk_devotional FOREIGN KEY (devotional_id) REFERENCES devotionals (id) ON DELETE CASCADE
);


CREATE INDEX idx_devotionals_search ON devotionals (active, status, title);