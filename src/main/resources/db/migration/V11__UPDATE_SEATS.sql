SET search_path = project, pg_catalog;

ALTER TABLE project.seats ADD COLUMN selected_at TIMESTAMP;
