package com.team13.junction.service

import com.team13.junction.model.SensorSubgroup
import com.team13.junction.model.SensorSubgroup.DISHWASHER_COLD
import com.team13.junction.model.SensorSubgroup.DISHWASHER_HOT
import com.team13.junction.model.SensorSubgroup.MIXER_COLD
import com.team13.junction.model.SensorSubgroup.MIXER_HOT
import com.team13.junction.model.SensorSubgroup.SHOWER_COLD
import com.team13.junction.model.SensorSubgroup.SHOWER_HOT
import com.team13.junction.model.SensorSubgroup.WASHER_COLD
import com.team13.junction.model.SensorSubgroup.WASHER_HOT

object ThresholdService {

    fun getThreshold(subgroup: SensorSubgroup): Double =
        when (subgroup) {
            SHOWER_HOT -> 10.0
            SHOWER_COLD -> 10.0
            WASHER_HOT -> 10.0
            WASHER_COLD -> 10.0
            DISHWASHER_HOT -> 10.0
            DISHWASHER_COLD -> 10.0
            MIXER_HOT -> 10.0
            MIXER_COLD -> 10.0
        }
}