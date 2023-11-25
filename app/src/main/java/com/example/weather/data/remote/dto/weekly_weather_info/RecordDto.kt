package com.example.weather.data.remote.dto.weekly_weather_info

import com.example.weather.domain.model.weekly_weather_info.Record

data class RecordDto(
    val locations: List<LocationDto>
) {
    fun toRecord(): Record {
        return Record(
            locations = locations.map { it.toLocation() }
        )
    }
}