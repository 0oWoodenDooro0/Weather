package com.example.weather.domain.model

data class Daily(
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val precipitation_probability_max: List<Int>,
    val uv_index_max: List<Double>,
    val weather_code: List<Int>
)