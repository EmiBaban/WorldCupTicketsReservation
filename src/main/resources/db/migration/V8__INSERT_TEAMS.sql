SET search_path = project, pg_catalog;

INSERT INTO teams (id, name, flag_url)
VALUES
    (gen_random_uuid(), 'Romania', 'https://countryflagsapi.com/png/ro'),
    (gen_random_uuid(), 'Brazil', 'https://countryflagsapi.com/png/br'),
    (gen_random_uuid(), 'Germany', 'https://countryflagsapi.com/png/de'),
    (gen_random_uuid(), 'France', 'https://countryflagsapi.com/png/fr'),
    (gen_random_uuid(), 'Argentina', 'https://countryflagsapi.com/png/ar'),
    (gen_random_uuid(), 'Spain', 'https://countryflagsapi.com/png/es'),
    (gen_random_uuid(), 'England', 'https://countryflagsapi.com/png/gb'),
    (gen_random_uuid(), 'Portugal', 'https://countryflagsapi.com/png/pt'),
    (gen_random_uuid(), 'Italy', 'https://countryflagsapi.com/png/it'),
    (gen_random_uuid(), 'USA', 'https://countryflagsapi.com/png/us');
