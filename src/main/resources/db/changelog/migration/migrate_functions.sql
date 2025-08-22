CREATE OR REPLACE FUNCTION refresh_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at := NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION touch_user_updated_at()
RETURNS TRIGGER AS $$
BEGIN
UPDATE "user"
    SET updated_at = NOW()
    WHERE id = COALESCE(OLD.user_id, NEW.user_id);
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;
