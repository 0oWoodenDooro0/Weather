package com.example.weather.data.remote.dto

data class HourlyUnitsDto(
    val is_day: String,
    val precipitation_probability: String,
    val temperature_2m: String,
    val time: String,
    val weather_code: String
)