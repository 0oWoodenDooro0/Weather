package com.example.weather.data.remote.dto

data class CurrentUnitsDto(
    val apparent_temperature: String,
    val interval: String,
    val is_day: String,
    val relative_humidity_2m: String,
    val surface_pressure: String,
    val temperature_2m: String,
    val time: String,
    val weather_code: String,
    val wind_direction_10m: String,
    val wind_speed_10m: String
)