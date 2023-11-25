package com.example.weather.data.remote.dto.weather_info

import com.example.weather.domain.model.weather_info.Parameter

data class ParameterDto(
    val parameterName: String?,
    val parameterUnit: String?,
    val parameterValue: String?
) {
    fun toParameter(): Parameter {
        return Parameter(
            parameterName = parameterName,
            parameterUnit = parameterUnit,
            parameterValue = parameterValue
        )
    }
}