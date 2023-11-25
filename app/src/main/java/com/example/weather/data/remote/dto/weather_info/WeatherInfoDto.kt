package com.example.weather.data.remote.dto.weather_info

import com.example.weather.domain.model.weather_info.WeatherInfo

data class WeatherInfoDto(
    val records: RecordDto,
    val result: ResultDto,
    val success: String
) {
    fun toWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            record = records.toRecord(),
            success = success
        )
    }
}