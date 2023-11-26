package com.example.weather.presentation

import com.example.weather.domain.model.WeatherInfo

data class WeatherInfoState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false
)
