CREATE TABLE IF NOT EXISTS Lecture (
    id serial PRIMARY KEY,
    title VARCHAR (256) not null,
    description VARCHAR (256) not null,
    creationDate VARCHAR (256) not null
);