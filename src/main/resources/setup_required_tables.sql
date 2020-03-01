CREATE TABLE IF NOT EXISTS apartment (
    id UUID PRIMARY KEY NOT NULL,
    offer_url VARCHAR (512) UNIQUE NOT NULL,
    address VARCHAR (512) NOT NULL,
    total_area NUMERIC (4, 2) NOT NULL CHECK (total_area > 0.0),
    living_space NUMERIC (4, 2) NOT NULL CHECK (living_space > 0.0),
    kitchen_area NUMERIC (4, 2) NOT NULL CHECK (kitchen_area > 0.0),
    rooms SMALLINT NOT NULL CHECK (rooms > 0),
    price_amount NUMERIC (20, 2) NOT NULL CHECK (price_amount > 0.0),
    price_currency VARCHAR (4) NOT NULL,
    last_checked_at TIMESTAMP NOT NULL,
    invalidated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS facility_type (
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR (255) NOT NULL,
    description VARCHAR (255) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS facility (
    id UUID PRIMARY KEY NOT NULL,
    facility_type_id UUID NOT NULL REFERENCES facility_type ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS apartment_facility (
    apartment_id UUID NOT NULL REFERENCES apartment ON UPDATE CASCADE ON DELETE CASCADE,
    facility_id UUID NOT NULL REFERENCES facility ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO facility_type (id, name, description)
VALUES
    ('12ffa84b-99d8-4fb3-910a-b10f44756261', 'bus', ''),
    ('da8decdc-1eb1-45e9-b03a-dd42b92dd9a9', 'underground', ''),
    ('c11d1133-6c64-48f0-afc6-d17d0df956e1', 'tram', ''),
    ('0a168458-2023-438c-a999-1902d3de35ab', 'supermarket', ''),
    ('87ba75e2-7735-4d4b-9915-a246d3c25b03', 'cafe', ''),
    ('1c3cbb9b-0704-42ee-9276-807ae8c3024b', 'restaurant', '')
;