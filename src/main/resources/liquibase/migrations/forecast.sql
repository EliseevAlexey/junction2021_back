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
