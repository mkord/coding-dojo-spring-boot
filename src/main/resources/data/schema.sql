CREATE TABLE weather (
    id uuid NOT NULL CONSTRAINT weather_pkey PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    temperature NUMERIC(5, 2),
    update_time TIMESTAMP NOT NULL
);
