--liquibase formatted sql

--changeset alex eliseev:1
--comment create anomaly table
create table if not exists public.anomaly
(
    id          bigserial primary key,
    timestamp   timestamp                               not null,
    building_id bigint references public.buildings (id) not null,
    block_id    bigint references public.blocks (id)    not null,
    sensor_id   bigint references public.sensors (id)   not null
);