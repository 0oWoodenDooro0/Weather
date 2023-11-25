package com.example.weather.data.remote.dto.weather_info

import com.example.weather.domain.model.weather_info.WeatherElement

data class WeatherElementDto(
    val elementName: String,
    val time: List<TimeDto>
) {
    fun toWeatherElement(): WeatherElement {
        return WeatherElement(
            elementName = elementName,
            times = time.map { it.toTime() }
        )
    }
}