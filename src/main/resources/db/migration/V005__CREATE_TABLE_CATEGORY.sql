CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR NOT NULL UNIQUE,
                            description VARCHAR,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP
);

CREATE TABLE user_categories (
                                 user_id BIGINT NOT NULL,
                                 category_id BIGINT NOT NULL,
                                 PRIMARY KEY (user_id, category_id),
                                 CONSTRAINT fk_user_category FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                                 CONSTRAINT fk_category_user FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE INDEX idx_user_categories_id ON user_categories (category_id);

INSERT INTO categories (id, name, description, active)
VALUES (1, 'Membro', 'Categoria padrão atribuída a todos os novos membros. Não pode ser excluída.', true);

SELECT setval('categories_id_seq', 1);