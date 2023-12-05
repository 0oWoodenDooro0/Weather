package com.example.weather.data.remote.dto

data class DailyUnitsDto(
    val sunrise: String,
    val sunset: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String,
    val precipitation_probability_max: String,
    val uv_index_max: String,
    val weather_code: String
)