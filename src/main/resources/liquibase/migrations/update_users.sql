--liquibase formatted sql

--changeset alex eliseev:1
--comment Add user role to users table
alter table if exists public.users
    add column role varchar(32) not null default 'USER';

alter table if exists public.users
    alter column name set not null;