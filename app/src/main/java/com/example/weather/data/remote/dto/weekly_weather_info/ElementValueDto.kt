package com.example.weather.data.remote.dto.weekly_weather_info

import com.example.weather.domain.model.weekly_weather_info.ElementValue

data class ElementValueDto(
    val measures: String,
    val value: String
) {
    fun toElementValue(): ElementValue {
        return ElementValue(
            measures = measures,
            value = value
        )
    }
}