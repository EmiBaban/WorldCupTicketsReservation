SET search_path = project, pg_catalog;

ALTER TABLE project.matches
    ADD COLUMN referee text,
    ADD COLUMN seat_price double precision NOT NULL;