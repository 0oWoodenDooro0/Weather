package com.example.weather.data.remote.dto

import com.example.weather.domain.model.Daily

data class DailyDto(
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>,
    val precipitation_probability_max: List<Int>,
    val uv_index_max: List<Double>,
    val weather_code: List<Int>
){
    fun toDaily():Daily{
        return Daily(
            temperature_2m_max = temperature_2m_max,
            temperature_2m_min = temperature_2m_min,
            time = time,
            precipitation_probability_max = precipitation_probability_max,
            uv_index_max = uv_index_max,
            weather_code = weather_code
        )
    }
}