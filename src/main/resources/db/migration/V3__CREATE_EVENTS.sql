SET search_path = project, pg_catalog;

CREATE TABLE stadiums (
                                        id uuid PRIMARY KEY,
                                        name text NOT NULL,
                                        capacity integer NOT NULL,
                                        description text,
                                        imageUrl text
);

CREATE TABLE teams (
                                     id uuid PRIMARY KEY,
                                     name text NOT NULL,
                                     flag_url text
);

CREATE TABLE matches (
                                       id uuid PRIMARY KEY,
                                       date_time timestamp NOT NULL,
                                       stadium_id uuid NOT NULL,
                                       home_team_id uuid NOT NULL,
                                       away_team_id uuid NOT NULL,
                                       FOREIGN KEY (stadium_id) REFERENCES project.stadiums (id),
                                       FOREIGN KEY (home_team_id) REFERENCES project.teams (id),
                                       FOREIGN KEY (away_team_id) REFERENCES project.teams (id)
);
