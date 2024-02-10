DROP TABLE IF EXISTS session_customer;
DROP TABLE IF EXISTS session;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS customer;

CREATE TABLE
    customer (
        id SERIAL,
        surname VARCHAR(32),
        name VARCHAR(32),

        PRIMARY KEY (id)
    );

CREATE TABLE
    movie (
        id SERIAL,
        title VARCHAR(128),

        PRIMARY KEY (id)
    );

CREATE TABLE
    session (
        id SERIAL,
        date_time TIMESTAMP(6),
        price NUMERIC,
        movie_id INTEGER,

        PRIMARY KEY (id),
        
        CONSTRAINT fk_movie 
            FOREIGN KEY (movie_id)
                REFERENCES movie (id)
                ON DELETE SET NULL
    );

CREATE TABLE
    session_customer (
        id SERIAL,
        registered_at TIMESTAMP(6),
        session_id INTEGER,
        customer_id INTEGER,

        PRIMARY KEY (id),

        CONSTRAINT fk_session 
            FOREIGN KEY (session_id)
                REFERENCES session (id)
                ON DELETE CASCADE,

        CONSTRAINT fk_customer 
            FOREIGN KEY (customer_id)
                REFERENCES customer (id)
                ON DELETE CASCADE
    );
