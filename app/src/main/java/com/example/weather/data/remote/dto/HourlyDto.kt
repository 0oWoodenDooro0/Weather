package com.example.weather.data.remote.dto

import com.example.weather.domain.model.Hourly

data class HourlyDto(
    val precipitation_probability: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>,
    val weather_code: List<Int>
) {
    fun toHourly(): Hourly {
        return Hourly(
            precipitation_probability,
            temperature_2m,
            time,
            weather_code
        )
    }
}