package com.example.weather.domain.use_case

import android.annotation.SuppressLint
import com.example.weather.core.util.Resource
import com.example.weather.domain.model.LatLng
import com.example.weather.domain.model.WeatherInfo
import com.example.weather.domain.repository.WeatherInfoRepository
import kotlinx.coroutines.flow.Flow

class GetWeatherInfo(
    private val repository: WeatherInfoRepository
) {
    @SuppressLint("SimpleDateFormat")
    operator fun invoke(latLng: LatLng): Flow<Resource<WeatherInfo>> {
        return repository.getWeatherInfo(latLng)
    }
}