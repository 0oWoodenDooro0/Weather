package com.example.weather.domain.model.weekly_weather_info

data class Location(
    val dataid: String,
    val datasetDescription: String,
    val location: List<LocationX>,
    val locationsName: String
)