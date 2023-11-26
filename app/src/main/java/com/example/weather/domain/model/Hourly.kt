package com.example.weather.domain.model

data class Hourly(
    val is_day: List<Int>,
    val precipitation_probability: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weather_code: List<Int>
)