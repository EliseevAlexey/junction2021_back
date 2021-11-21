--liquibase formatted sql


--changeset alex eliseev:1
--comment create water_forecast table
create table if not exists water_forecast_2
(
    date          timestamp        not null,
    water_neutral double precision not null,
    water_lower   double precision not null,
    water_upper   double precision not null,
    building_id   bigint           not null,
    block_id      bigint           not null,
    sensor_id     bigint           not null,
    id            serial
        constraint water_forecast_2_pkey
            primary key
);

--changeset alex eliseev:2
--comment create energy_forecast table
create table if not exists energy_forecast
(
    date          timestamp        not null,
    water_neutral double precision not null,
    energy_lower  double precision not null,
    energy_upper  double precision not null,
    building_id   bigint           not null,
    block_id      bigint           not null,
    sensor_id     bigint           not null,
    id            serial primary key
);

--changeset alex eliseev:3
--comment rename fields in energy_forecast

alter table public.energy_forecast
rename column energy_lower to water_lower;
alter table public.energy_forecast
rename column energy_upper to water_upper