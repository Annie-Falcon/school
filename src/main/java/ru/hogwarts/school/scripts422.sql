CREATE TABLE car (
    id_car SERIAL PRIMARY KEY,
    mark   VARCHAR(50),
	model  VARCHAR(50),
    cost   NUMERIC(13, 2)
);

CREATE TABLE driver (
    id_driver SERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
	age       INTEGER,
    is_driver_license BOOLEAN NOT NULL DEFAULT false,
	id_car    SERIAL REFERENCES car (id_car)   
);