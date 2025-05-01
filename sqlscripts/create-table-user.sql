CREATE TABLE public."AppUser"
(
    id bigserial NOT NULL,
    name character varying(50),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public."AppUser"
    OWNER to paulodb;