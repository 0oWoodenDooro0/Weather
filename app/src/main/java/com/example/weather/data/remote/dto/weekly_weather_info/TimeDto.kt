package com.example.weather.data.remote.dto.weekly_weather_info

import com.example.weather.domain.model.weekly_weather_info.Time

data class TimeDto(
    val elementValue: List<ElementValueDto>,
    val endTime: String,
    val startTime: String
) {
    fun toTime(): Time {
        return Time(
            elementValue = elementValue.map { it.toElementValue() },
            endTime = endTime,
            startTime = startTime
        )
    }
}