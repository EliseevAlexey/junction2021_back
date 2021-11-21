--liquibase formatted sql

--changeset alex eliseev:1
--comment Create meetup table
create table if not exists public.meetups
(
    id         bigserial primary key,
    type       text      not null,
    name       text      not null,
    start_date timestamp not null,
    end_date   timestamp not null,
    tags       text[]    not null default array []::text[],
    cover      text,
    point      GEOGRAPHY
);


--changeset alex eliseev:2
--comment Create meetup participant table
create table if not exists public.meetup_participant
(
    meetup_id bigint references public.meetups (id),
    primary key (meetup_id)
);


--changeset alex eliseev:3
--comment Add description field for meetup table
alter table public.meetups
add column description text;
