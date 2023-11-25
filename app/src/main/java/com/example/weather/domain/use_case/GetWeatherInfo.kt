package com.example.weather.domain.use_case

import android.annotation.SuppressLint
import com.example.weather.core.util.Resource
import com.example.weather.domain.model.weather_info.WeatherInfo
import com.example.weather.domain.repository.WeatherInfoRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GetWeatherInfo(
    private val repository: WeatherInfoRepository
) {
    @SuppressLint("SimpleDateFormat")
    operator fun invoke(location: String, currentTime: LocalDateTime): Flow<Resource<WeatherInfo>> {
        return repository.getWeatherInfo(
            location,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(currentTime)
        )
    }
}