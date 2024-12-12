CREATE TABLE IF NOT EXISTS users (id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, user_name varchar(250), email varchar(254) UNIQUE);
CREATE TABLE IF NOT EXISTS categories (id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, categorie_name varchar(50) UNIQUE);
CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation varchar(2000),
    category_id BIGINT,
    created_on timestamp,
    description varchar(7000),
    event_date timestamp,
    initiator_id BIGINT,
    location_lat FLOAT,
    location_lon FLOAT,
    paid boolean,
    participant_limit integer,
    published_on timestamp,
    request_moderation boolean,
    state varchar(50),
    title varchar(200),
    CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
    CONSTRAINT fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created timestamp,
    event_id BIGINT,
    requester_id BIGINT,
    status varchar(50),
    CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id) );

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned boolean,
    title varchar(50) );

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT,
    event_id BIGINT,
    CONSTRAINT PK_compilations_events PRIMARY KEY (compilation_id, event_id) );

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text varchar(5000),
    commentator_id BIGINT,
    event_id BIGINT,
    created timestamp,
    status varchar(50),
    CONSTRAINT fk_comments_to_events FOREIGN KEY(event_id) REFERENCES events(id),
    CONSTRAINT fk_comments_to_users FOREIGN KEY(commentator_id) REFERENCES users(id) );

CREATE TABLE IF NOT EXISTS complaints (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description varchar(5000),
    comment_id BIGINT,
    created timestamp,
    status varchar(50),
    CONSTRAINT fk_complaints_to_comments FOREIGN KEY(comment_id) REFERENCES comments(id) );



