package com.example.weather.domain.model.weather_info

data class WeatherElement(
    val elementName: String,
    val times: List<Time>
)
