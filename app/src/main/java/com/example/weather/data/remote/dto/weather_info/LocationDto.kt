package com.example.weather.data.remote.dto.weather_info

import com.example.weather.domain.model.weather_info.Location

data class LocationDto(
    val locationName: String,
    val weatherElement: List<WeatherElementDto>
) {
    fun toLocation(): Location {
        return Location(
            locationName = locationName,
            weatherElements = weatherElement.map { it.toWeatherElement() }
        )
    }
}