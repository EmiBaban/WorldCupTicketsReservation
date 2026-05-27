SET search_path = project, pg_catalog;

ALTER TABLE project.stadiums
    ADD country VARCHAR(255),
    ADD city VARCHAR(255);