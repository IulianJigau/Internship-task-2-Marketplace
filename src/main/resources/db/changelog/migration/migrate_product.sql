CREATE TABLE IF NOT EXISTS "product" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    is_deleted BOOLEAN NOT NULL DEFAULT false
);

ALTER TABLE product
ADD COLUMN IF NOT EXISTS fts_vector tsvector;

CREATE INDEX IF NOT EXISTS idx_product_fts_vector
ON product
USING GIN (fts_vector);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger WHERE tgname = 'set_product_fts_vector'
    ) THEN
        CREATE TRIGGER set_product_fts_vector
        BEFORE INSERT OR UPDATE ON product
        FOR EACH ROW
        EXECUTE FUNCTION tsvector_update_trigger(
            fts_vector, 'pg_catalog.english', name
        );
    END IF;
END;
$$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger WHERE tgname = 'set_product_updated_at'
    ) THEN
        CREATE TRIGGER set_product_updated_at
        BEFORE UPDATE ON product
        FOR EACH ROW
        EXECUTE FUNCTION refresh_updated_at();
    END IF;
END;
$$;
