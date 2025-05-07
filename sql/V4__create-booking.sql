CREATE TABLE public.booking
(
    id bigserial NOT NULL,
    property_id bigint,
    user_id bigint,
    date_range_id bigint,
    number_of_guests integer,
    booking_status integer,
    total_price double precision,
    PRIMARY KEY (id),
    CONSTRAINT fk_property_id FOREIGN KEY (property_id) REFERENCES public.property (id),
    CONSTRAINT fk_app_user_id FOREIGN KEY (user_id) REFERENCES public.app_user (id),
    CONSTRAINT fk_date_range_id FOREIGN KEY (date_range_id) REFERENCES public.date_range (id)
);

ALTER TABLE IF EXISTS public.booking
    OWNER to paulodb;