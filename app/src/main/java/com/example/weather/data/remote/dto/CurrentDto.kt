package com.example.weather.data.remote.dto

import com.example.weather.domain.model.Current

data class CurrentDto(
    val apparent_temperature: Double,
    val interval: Int,
    val relative_humidity_2m: Int,
    val surface_pressure: Double,
    val temperature_2m: Double,
    val time: String,
    val weather_code: Int,
    val wind_direction_10m: Int,
    val wind_speed_10m: Double
) {
    fun toCurrent(): Current {
        return Current(
            apparent_temperature,
            relative_humidity_2m,
            surface_pressure,
            temperature_2m,
            time,
            weather_code,
            wind_direction_10m,
            wind_speed_10m
        )
    }
}