package com.example.weather.data.remote.dto

import com.example.weather.domain.model.Daily

data class DailyDto(
    val sunrise: List<String>,
    val sunset: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val precipitation_probability_max: List<Int>,
    val uv_index_max: List<Double>,
    val weather_code: List<Int>,
    val wind_direction_10m_dominant: List<Int>,
    val wind_speed_10m_max: List<Double>
) {
    fun toDaily(): Daily {
        return Daily(
            sunrise,
            sunset,
            temperature_2m_max,
            temperature_2m_min,
            time,
            precipitation_probability_max,
            uv_index_max,
            weather_code,
            wind_direction_10m_dominant,
            wind_speed_10m_max
        )
    }
}