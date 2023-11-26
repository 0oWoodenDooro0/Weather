package com.example.weather.data.remote.dto

import com.example.weather.domain.model.Hourly

data class HourlyDto(
    val is_day: List<Int>,
    val precipitation_probability: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weather_code: List<Int>
) {
    fun toHourly(): Hourly {
        return Hourly(
            is_day = is_day,
            precipitation_probability = precipitation_probability,
            temperature_2m = temperature_2m,
            time = time,
            weather_code = weather_code
        )
    }
}