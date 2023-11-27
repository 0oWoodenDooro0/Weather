package com.example.weather.domain.model

data class Current(
    val apparent_temperature: Double,
    val is_day: Int,
    val relative_humidity_2m: Int,
    val surface_pressure: Double,
    val temperature_2m: Double,
    val time: String,
    val weather_code: Int,
    val wind_direction_10m: Int,
    val wind_speed_10m: Double
)