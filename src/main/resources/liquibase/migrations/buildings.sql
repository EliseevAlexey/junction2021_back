--liquibase formatted sql


--changeset alex eliseev:1
--comment Create building table
create table if not exists public.buildings
(
    id    bigserial primary key,
    name  text      not null,
    point GEOGRAPHY
);


--changeset alex eliseev:2
--comment Create users_buildings table
create table if not exists public.users_buildings
(
    user_id     bigint REFERENCES public.users (id),
    building_id bigint REFERENCES public.buildings (id),
    PRIMARY KEY (user_id, building_id)
);
