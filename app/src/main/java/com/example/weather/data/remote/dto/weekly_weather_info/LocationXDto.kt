package com.example.weather.data.remote.dto.weekly_weather_info

import com.example.weather.domain.model.weekly_weather_info.LocationX

data class LocationXDto(
    val geocode: String,
    val lat: String,
    val locationName: String,
    val lon: String,
    val weatherElement: List<WeatherElementDto>
) {
    fun toLocationX(): LocationX {
        return LocationX(
            geocode = geocode,
            lat = lat,
            locationName = locationName,
            lon = lon,
            weatherElement = weatherElement.map { it.toWeatherElement() }
        )
    }
}