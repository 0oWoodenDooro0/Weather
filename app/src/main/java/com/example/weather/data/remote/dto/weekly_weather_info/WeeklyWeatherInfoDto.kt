package com.example.weather.data.remote.dto.weekly_weather_info

import com.example.weather.domain.model.weekly_weather_info.WeeklyWeatherInfo

data class WeeklyWeatherInfoDto(
    val records: RecordDto,
    val result: ResultDto,
    val success: String
) {
    fun toWeeklyWeatherInfo(): WeeklyWeatherInfo {
        return WeeklyWeatherInfo(
            records = records.toRecord(),
            success = success
        )
    }
}