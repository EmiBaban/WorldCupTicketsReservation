SET search_path = project, pg_catalog;

CREATE TABLE feedback (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          category VARCHAR(255),
                          message TEXT,
                          rating INTEGER NOT NULL,
                          recommend BOOLEAN NOT NULL,
                          submitted_at TIMESTAMP WITHOUT TIME ZONE,
                          user_id UUID,
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

