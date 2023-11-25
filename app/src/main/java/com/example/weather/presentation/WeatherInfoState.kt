package com.example.weather.presentation

import com.example.weather.domain.model.weather_info.WeatherInfo
import com.example.weather.domain.model.weekly_weather_info.WeeklyWeatherInfo

data class WeatherInfoState(
    val weatherInfo: WeatherInfo? = null,
    val weeklyWeatherInfo: WeeklyWeatherInfo? = null,
    val isLoading: Boolean = false
)
