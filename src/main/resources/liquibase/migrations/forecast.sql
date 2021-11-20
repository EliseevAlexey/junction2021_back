--liquibase formatted sql


--changeset alex eliseev:1
--comment create forecast table
create table if not exists forecast
(
    ds          timestamp        not null,
    yhat        double precision not null,
    yhat_lower  double precision not null,
    yhat_upper  double precision,
    building_id bigint           not null,
    block_id    bigint           not null,
    sensor_id   bigint           not null,
    constraint forecast_pk
        unique (ds, sensor_id)
);

create index if not exists forecast_ds_building_id_block_id_sensor_id_index
    on forecast (ds, building_id, block_id, sensor_id);
