--liquibase formatted sql

--changeset alex eliseev:1
--comment Create sensors table
create table if not exists public.sensors
(
    id       bigserial primary key,
    name     text                                 not null,
    block_id bigint references public.blocks (id) not null,
    type     text                                 not null,
    data     JSONB                                not null DEFAULT '[]'
);
