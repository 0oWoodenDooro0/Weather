package com.example.weather.data.remote.dto.weekly_weather_info

import com.example.weather.domain.model.weekly_weather_info.WeatherElement

data class WeatherElementDto(
    val description: String,
    val elementName: String,
    val time: List<TimeDto>
) {
    fun toWeatherElement(): WeatherElement {
        return WeatherElement(
            description = description,
            elementName = elementName,
            time = time.map { it.toTime() }
        )
    }
}