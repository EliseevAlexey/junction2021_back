package com.team13.junction.model.ui

import com.team13.junction.model.SensorGroup
import com.team13.junction.model.ValueUnit
import org.locationtech.jts.geom.Geometry
import java.time.LocalDateTime

data class MainUiPage(
    val buildings: List<BuildingUiDto>,
    val totals: List<TotalUiDto>,
)

data class BuildingUiDto(
    val id: Long,
    val name: String,
    val point: Geometry?,
    val blocks: List<BlockUiDto>,
)

data class BlockUiDto(
    val id: Long,
    val name: String,
    val charts: Map<SensorGroup, Chart>,
    val sensors: List<SensorUiDto>,
)

data class SensorUiDto(
    val id: Long,
    val name: String,
    val charts: Map<SensorGroup, Chart>,
)

data class TotalUiDto(
    val sensorGroup: SensorGroup,
    val values: Double,
    val unit: ValueUnit,
)

data class Chart(
    val threshold: Double,
    val data: List<ChartItem>,
)

data class ChartItem(
    val date: LocalDateTime,
    val value: Double,
)
