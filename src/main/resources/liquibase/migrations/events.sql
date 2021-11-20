--liquibase formatted sql


--changeset alex eliseev:1
--comment Create events table
create table if not exists public.events
(
    id      bigserial primary key,
    data    text not null,
    is_sent bool default false
);
