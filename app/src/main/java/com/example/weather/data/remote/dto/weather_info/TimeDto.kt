package com.example.weather.data.remote.dto.weather_info

import com.example.weather.domain.model.weather_info.Time

data class TimeDto(
    val endTime: String,
    val parameter: ParameterDto,
    val startTime: String
) {
    fun toTime(): Time {
        return Time(
            endTime = endTime,
            parameter = parameter.toParameter(),
            startTime = startTime
        )
    }
}