SET search_path = project, pg_catalog;

CREATE TABLE feedbacks (
                           id uuid PRIMARY KEY,
                           category text NOT NULL,
                           message text NOT NULL,
                           rating integer NOT NULL CHECK (rating >= 1 AND rating <= 5),
                           recommend boolean NOT NULL,
                           user_id uuid NOT NULL,
                           submitted_at timestamp NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES project.users (id)
);
