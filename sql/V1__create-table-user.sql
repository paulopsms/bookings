CREATE TABLE public."app_user"
(
    id bigserial NOT NULL,
    name character varying(50),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public."app_user"
    OWNER to paulodb;