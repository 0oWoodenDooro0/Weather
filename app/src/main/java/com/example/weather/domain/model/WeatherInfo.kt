package com.example.weather.domain.model

data class WeatherInfo(
    val current: Current,
    val daily: Daily,
    val hourly: Hourly,
    val latitude: Double,
    val longitude: Double
)