package com.example.weather.domain.model.weekly_weather_info

data class Time(
    val elementValue: List<ElementValue>,
    val endTime: String,
    val startTime: String
)