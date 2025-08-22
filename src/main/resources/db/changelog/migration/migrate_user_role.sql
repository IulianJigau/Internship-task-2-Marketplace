CREATE TABLE IF NOT EXISTS "user_role" (
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger WHERE tgname = 'set_product_updated_at_on_role_change'
    ) THEN
        CREATE TRIGGER set_product_updated_at_on_role_change
        AFTER INSERT OR DELETE ON user_role
        FOR EACH ROW
        EXECUTE FUNCTION touch_user_updated_at();
    END IF;
END
$$;