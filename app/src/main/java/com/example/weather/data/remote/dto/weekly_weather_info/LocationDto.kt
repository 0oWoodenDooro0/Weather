package com.example.weather.data.remote.dto.weekly_weather_info

import com.example.weather.domain.model.weekly_weather_info.Location

data class LocationDto(
    val dataid: String,
    val datasetDescription: String,
    val location: List<LocationXDto>,
    val locationsName: String
) {
    fun toLocation(): Location {
        return Location(
            dataid = dataid,
            datasetDescription = datasetDescription,
            location = location.map { it.toLocationX() },
            locationsName = locationsName
        )
    }
}