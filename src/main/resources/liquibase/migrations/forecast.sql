--liquibase formatted sql


--changeset alex eliseev:1
--comment create water_forecast table
create table if not exists water_forecast
(
    index              bigint    not null
        constraint forecast_pk
            primary key,
    date               timestamp not null,
    cold_water_neutral double precision,
    cold_water_lower   double precision,
    cold_water_upper   double precision,
    hot_water_neutral  double precision,
    hot_water_lower    double precision,
    hot_water_upper    double precision,
    building_id        bigint    not null,
    block_id           bigint    not null,
    sensor_id          bigint    not null
);
create index if not exists ix_public_water_forecast_index
    on water_forecast (index);
