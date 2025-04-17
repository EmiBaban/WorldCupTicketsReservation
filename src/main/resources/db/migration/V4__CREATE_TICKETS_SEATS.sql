SET search_path = project, pg_catalog;

CREATE TABLE seats (
                       id uuid PRIMARY KEY,
                       row text NOT NULL,
                       number integer NOT NULL,
                       status text CHECK (status IN ('LIBER', 'OCUPAT', 'SELECTAT')),
                       match_id uuid NOT NULL,
                       FOREIGN KEY (match_id) REFERENCES project.matches (id)
);

CREATE TABLE tickets (
                         id uuid PRIMARY KEY,
                         user_id uuid NOT NULL,
                         match_id uuid NOT NULL,
                         seat_id uuid NOT NULL,
                         price double precision NOT NULL,
                         purchase_time timestamp NOT NULL,
                         is_paid boolean NOT NULL,
                         payment_method text,
                         FOREIGN KEY (user_id) REFERENCES project.users (id),
                         FOREIGN KEY (match_id) REFERENCES project.matches (id),
                         FOREIGN KEY (seat_id) REFERENCES project.seats (id)
);
