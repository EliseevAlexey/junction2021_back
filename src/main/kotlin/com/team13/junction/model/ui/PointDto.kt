package com.team13.junction.model.ui

import org.locationtech.jts.geom.Geometry

data class PointDto(
    val lat: Double,
    val lon: Double
)

fun Geometry.toPointDto() = this.interiorPoint.let { point ->
    PointDto(
        lat = point.x,
        lon = point.y,
    )
}