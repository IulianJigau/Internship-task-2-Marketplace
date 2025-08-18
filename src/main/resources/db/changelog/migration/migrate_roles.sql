--
-- Name: roles; Type: TABLE; Schema: public; Owner: market_user
--
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: market_user
--
CREATE TABLE user_role (
    user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

