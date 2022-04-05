CREATE TABLE IF NOT EXISTS Homework (
    id serial PRIMARY KEY,
    task VARCHAR (256) not null,
    deadline VARCHAR (256) not null
);