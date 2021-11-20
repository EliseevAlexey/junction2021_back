package com.team13.junction.model

import com.team13.junction.model.ui.Chart

data class BlockData(
    val blockId: Long,
    val blockName: String,
    val charts: Map<SensorGroup, Chart>,
    val sensorDatas: List<SensorModel>,
)
