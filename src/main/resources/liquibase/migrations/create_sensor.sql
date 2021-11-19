--liquibase formatted sql

--changeset alex eliseev:1
--comment Create sensors data
create type sensor_type as enum (
    'SHOWER', 'WASHER', 'DISHWASHER', 'MIXER'
);

create table if not exists public.sensors
(
    id       bigserial primary key,
    name     varchar(128) not null ,
    block_id bigint references public.blocks (id) not null,
    type     sensor_type                         not null,
    data     JSONB                               not null DEFAULT '[]'
);
