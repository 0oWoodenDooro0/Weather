package com.example.weather.data.remote.dto

import com.example.weather.domain.model.WeatherInfo

data class WeatherInfoDto(
    val current: CurrentDto,
    val current_units: CurrentUnitsDto,
    val daily: DailyDto,
    val daily_units: DailyUnitsDto,
    val elevation: Int,
    val generationtime_ms: Double,
    val hourly: HourlyDto,
    val hourly_units: HourlyUnitsDto,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
) {
    fun toWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            current = current.toCurrent(),
            daily = daily.toDaily(),
            hourly = hourly.toHourly(),
            latitude = latitude,
            longitude = longitude
        )
    }
}