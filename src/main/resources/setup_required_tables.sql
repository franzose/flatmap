CREATE TABLE IF NOT EXISTS property (
    id UUID PRIMARY KEY NOT NULL,
    offer_url VARCHAR (512) UNIQUE NOT NULL,
    address VARCHAR (512) NOT NULL,
    total_area NUMERIC (12, 2) NOT NULL CHECK (total_area > 0.0),
    living_space NUMERIC (12, 2) NOT NULL CHECK (living_space > 0.0),
    kitchen_area NUMERIC (12, 2) NOT NULL CHECK (kitchen_area > 0.0),
    rooms SMALLINT NOT NULL CHECK (rooms > 0),
    price_amount NUMERIC (20, 2) NOT NULL CHECK (price_amount > 0.0),
    price_currency VARCHAR (4) NOT NULL,
    last_checked_at TIMESTAMP NULL,
    invalidated_at TIMESTAMP NULL
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

CREATE TABLE IF NOT EXISTS property_facility (
    property_id UUID NOT NULL REFERENCES property ON UPDATE CASCADE ON DELETE CASCADE,
    facility_id UUID NOT NULL REFERENCES facility ON UPDATE CASCADE ON DELETE CASCADE
);
