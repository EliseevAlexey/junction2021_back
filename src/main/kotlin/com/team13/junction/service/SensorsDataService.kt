package com.team13.junction.service

import com.team13.junction.dao.SensorDataDao
import com.team13.junction.model.SensorData
import com.team13.junction.model.SensorDataJson
import org.springframework.stereotype.Service

@Service
class SensorsDataService(
    private val dao: SensorDataDao,
    private val blockService: BlockService,
    private val buildingService: BuildingService,
    private val sensorService: SensorService,
    private val mlService: MLService,
) {

    fun create(sensorId: Long, dto: SensorDataJson): SensorData {
        val sensor = sensorService.getById(sensorId)
        // todo rewrite this part with joins
        val block = blockService.get(sensor.block.id)
        val building = buildingService.get(block.building.id)

        mlService.predictData(building.id, block.id, sensor.id)

        return dao.save(
            SensorData(
                block = block,
                building = building,
                sensor = sensor,
                type = sensor.sensorSubgroup,
                data = dto,
            )
        )
    }
}

