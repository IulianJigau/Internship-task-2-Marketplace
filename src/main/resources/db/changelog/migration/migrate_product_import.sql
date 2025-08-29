CREATE TABLE IF NOT EXISTS "product_import"
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    description TEXT
);