CREATE TABLE IF NOT EXISTS users (
    user_id INT GENERATED BY DEFAULT AS IDENTITY,
    email varchar NOT NULL,
    name varchar,
    login varchar NOT NULL,
    birthday date NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (user_id)
    );

CREATE TABLE IF NOT EXISTS friends (
    user_id int NOT NULL,
    friend_id int NOT NULL,
    status varchar,
    CONSTRAINT PK_FRIENDS PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes (
    user_id int NOT NULL,
    film_id int NOT NULL,
    CONSTRAINT PK_LIKES PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS films (
    film_id INT GENERATED BY DEFAULT AS IDENTITY,
    film_name varchar NOT NULL,
    description varchar(200) NOT NULL,
    rating_id varchar NOT NULL,
    release_date date,
    duration int,
    CONSTRAINT PK_FILMS PRIMARY KEY (film_id)
);

CREATE TABLE IF NOT EXISTS MPA_rating (
    rating_id int NOT NULL,
    rating_name varchar NOT NULL,
    CONSTRAINT PK_MPA_RATING PRIMARY KEY (rating_id)
);

CREATE TABLE IF NOT EXISTS genre (
    film_id int NOT NULL,
    genre_id int NOT NULL,
    CONSTRAINT PK_GENRE  PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id int NOT NULL,
    genre_name varchar NOT NULL,
    CONSTRAINT PK_GENRES PRIMARY KEY (genre_id)
);

ALTER TABLE  friends
    ADD FOREIGN KEY  (user_id)  REFERENCES users(user_id);

ALTER TABLE  friends
    ADD FOREIGN KEY  (friend_id)  REFERENCES users(user_id);

ALTER TABLE likes
    ADD FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE likes
    ADD FOREIGN KEY (film_id) REFERENCES films (film_id);

ALTER TABLE  films
    ADD FOREIGN KEY (rating_id) REFERENCES MPA_rating(rating_id);

ALTER TABLE genre
    ADD FOREIGN KEY (film_id) REFERENCES films(film_id);

ALTER TABLE genre
    ADD FOREIGN KEY (genre_id) REFERENCES genres (genre_id);

