SET search_path = project, pg_catalog;

INSERT INTO teams (id, name, flag_url)
VALUES
    (gen_random_uuid(), 'Romania', 'https://countryflagsapi.com/png/ro'),
    (gen_random_uuid(), 'Brazil', 'https://countryflagsapi.com/png/br'),
    (gen_random_uuid(), 'Germany', 'https://countryflagsapi.com/png/de');