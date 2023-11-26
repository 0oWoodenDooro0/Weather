package com.example.weather.domain.repository

import com.example.weather.core.util.Resource
import com.example.weather.domain.model.LatLng
import com.example.weather.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherInfoRepository {

    fun getWeatherInfo(latLng: LatLng): Flow<Resource<WeatherInfo>>
}