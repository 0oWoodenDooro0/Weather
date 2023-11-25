package com.example.weather.data.remote.dto.weather_info

data class ResultDto(
    val fields: List<FieldDto>,
    val resource_id: String
)