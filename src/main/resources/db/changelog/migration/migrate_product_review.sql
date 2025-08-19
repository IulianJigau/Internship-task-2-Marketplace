CREATE TABLE IF NOT EXISTS product_review (
    product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    positive BOOLEAN,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (product_id, user_id)
);
