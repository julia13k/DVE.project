CREATE TABLE IF NOT EXISTS Resourse (
    id serial PRIMARY KEY,
    resourse_name VARCHAR (256) not null,
    data VARCHAR (256) not null,
    type VARCHAR (256) not null
)