package com.example.weather.domain.model

data class Daily(
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val precipitation_probability_max: List<Int>,
    val uv_index_max: List<Double>,
    val weather_code: List<Int>,
    val wind_direction_10m_dominant: List<Int>,
    val wind_speed_10m_max: List<Double>
)