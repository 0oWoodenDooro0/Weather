package com.example.weather.domain.model.weekly_weather_info

data class LocationX(
    val geocode: String,
    val lat: String,
    val locationName: String,
    val lon: String,
    val weatherElement: List<WeatherElement>
)