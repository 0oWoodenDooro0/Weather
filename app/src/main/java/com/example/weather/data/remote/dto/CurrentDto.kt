package com.example.weather.data.remote.dto

import com.example.weather.domain.model.Current

data class CurrentDto(
    val apparent_temperature: Double,
    val interval: Int,
    val is_day: Int,
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
            apparent_temperature = apparent_temperature,
            is_day = is_day,
            relative_humidity_2m = relative_humidity_2m,
            surface_pressure = surface_pressure,
            temperature_2m = temperature_2m,
            time = time,
            weather_code = weather_code,
            wind_direction_10m = wind_direction_10m,
            wind_speed_10m = wind_speed_10m
        )
    }
}