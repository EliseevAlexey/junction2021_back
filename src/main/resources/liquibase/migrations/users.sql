--liquibase formatted sql


--changeset alex eliseev:1
--comment Create users table
create table if not exists public.users
(
    id   bigserial primary key,
    name text not null,
    role text not null default 'USER'
);
