package com.example.weather.data.remote.dto.weather_info

import com.example.weather.domain.model.weather_info.Record

data class RecordDto(
    val datasetDescription: String,
    val location: List<LocationDto>
) {
    fun toRecord(): Record {
        return Record(
            datasetDescription = datasetDescription,
            locations = location.map { it.toLocation() }
        )
    }
}