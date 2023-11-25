package com.example.weather.domain.model.weather_info

data class Location(
    val locationName: String,
    val weatherElements: List<WeatherElement>
)
