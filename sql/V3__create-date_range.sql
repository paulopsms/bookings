CREATE TABLE public.date_range
(
    id bigserial NOT NULL,
    start_date date,
    end_date date,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.date_range
    OWNER to paulodb;