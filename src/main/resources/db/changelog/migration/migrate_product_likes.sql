--
-- Name: product_likes; Type: TABLE; Schema: public; Owner: market_user
--
CREATE TABLE IF NOT EXISTS "product_likes"
(
    product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    PRIMARY KEY (product_id, user_id)
);