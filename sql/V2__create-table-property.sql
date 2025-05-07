-- Table: public.property

-- DROP TABLE IF EXISTS public.property;

CREATE TABLE IF NOT EXISTS public.property
(
    id bigserial NOT NULL,
    name character varying(30),
    description character varying(70),
    number_of_guests integer,
    base_price_per_night double precision,
    CONSTRAINT property_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.property
    OWNER to paulodb;