package com.example.weather.domain.model.weekly_weather_info

data class WeatherElement(
    val description: String,
    val elementName: String,
    val time: List<Time>
)