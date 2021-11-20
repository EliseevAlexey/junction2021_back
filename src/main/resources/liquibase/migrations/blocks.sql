--liquibase formatted sql


--changeset alex eliseev:1
--comment create blocks table
create table if not exists public.blocks
(
    id          bigserial primary key,
    name        text                                    not null,
    building_id bigint references public.buildings (id) not null
);
