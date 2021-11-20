package com.team13.junction.model

enum class SensorSubgroup(val group: SensorGroup) {
    SHOWER_HOT(SensorGroup.WATER_HOT), SHOWER_COLD(SensorGroup.WATER_COLD),
    WASHER_HOT(SensorGroup.WATER_HOT), WASHER_COLD(SensorGroup.WATER_COLD),
    DISHWASHER_HOT(SensorGroup.WATER_HOT), DISHWASHER_COLD(SensorGroup.WATER_COLD),
    MIXER_HOT(SensorGroup.WATER_HOT), MIXER_COLD(SensorGroup.WATER_COLD),
}
