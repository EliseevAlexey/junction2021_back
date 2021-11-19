--liquibase formatted sql

--changeset alex eliseev:1
--comment Create building
create table if not exists public.buildings
(
    id       bigserial primary key,
    name     varchar(128) not null,
    location GEOGRAPHY    not null
);

create table if not exists public.users_buildings
(
    user_id     bigint REFERENCES public.users (id),
    building_id bigint REFERENCES public.buildings (id),
    PRIMARY KEY (user_id, building_id)
);

create table if not exists public.blocks
(
    id          bigserial primary key,
    name        varchar(128)                            not null,
    building_id bigint references public.buildings (id) not null
);


