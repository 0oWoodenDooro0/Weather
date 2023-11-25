package com.example.weather.domain.repository

import com.example.weather.core.util.Resource
import com.example.weather.domain.model.weather_info.WeatherInfo
import com.example.weather.domain.model.weekly_weather_info.WeeklyWeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherInfoRepository {

    fun getWeatherInfo(location: String, timeFrom: String): Flow<Resource<WeatherInfo>>

    fun getWeeklyWeatherInfo(location: String, timeFrom: String): Flow<Resource<WeeklyWeatherInfo>>
}