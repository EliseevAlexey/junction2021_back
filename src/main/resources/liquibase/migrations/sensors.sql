--liquibase formatted sql

--changeset alex eliseev:1
--comment Create sensors table
create table if not exists public.sensors
(
    id       bigserial primary key,
    name     text                                 not null,
    block_id bigint references public.blocks (id) not null,
    type     text                                 not null
);


--changeset alex eliseev:2
--comment Create sensors data table
create table if not exists public.sensors_data
(
    id          bigserial primary key,
    timestamp   timestamp                               not null,
    building_id bigint references public.buildings (id) not null,
    block_id    bigint references public.blocks (id)    not null,
    sensor_id   bigint references public.sensors (id)   not null,
    data        JSONB                                   not null
);

--changeset alex eliseev:3
--comment Add column sensor type
alter table if exists public.sensors_data
    ADD COLUMN sensor_type text not null;


