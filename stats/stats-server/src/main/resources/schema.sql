
CREATE TABLE IF NOT EXISTS hits (id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, app varchar(200), uri varchar(200), ip varchar(200), hit_timestamp timestamp );
