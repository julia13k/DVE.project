CREATE TABLE IF NOT EXISTS Course (
    id serial PRIMARY KEY,
    name VARCHAR (256) not null
);

CREATE TABLE IF NOT EXISTS Homework (
    id serial PRIMARY KEY,
    task VARCHAR (256) not null,
    deadline VARCHAR (256) not null
);

CREATE TABLE IF NOT EXISTS Lecture (
    id serial PRIMARY KEY,
    title VARCHAR (256) not null,
    description VARCHAR (256) not null,
    creationDate VARCHAR (256) not null
);

CREATE TABLE IF NOT EXISTS Person (
    id serial PRIMARY KEY,
    firstName VARCHAR (256) not null,
    lastName VARCHAR (256) not null,
    contacts VARCHAR (256) not null,
    email VARCHAR (256) not null,
    role VARCHAR (256) not null
);

CREATE TABLE IF NOT EXISTS Resourse (
    id serial PRIMARY KEY,
    resourse_name VARCHAR (256) not null,
    data VARCHAR (256) not null,
    type VARCHAR (256) not null
)