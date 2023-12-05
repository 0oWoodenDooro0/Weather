package com.example.weather.data.remote

import com.example.weather.data.remote.dto.WeatherInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v1/forecast")
    suspend fun getWeatherInfo(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: Array<String> = arrayOf(
            "temperature_2m",
            "relative_humidity_2m",
            "apparent_temperature",
            "weather_code",
            "surface_pressure",
            "wind_speed_10m",
            "wind_direction_10m"
        ),
        @Query("hourly") hourly: Array<String> = arrayOf(
            "temperature_2m",
            "precipitation_probability",
            "weather_code",
        ),
        @Query("daily", encoded = true) daily: Array<String> = arrayOf(
            "weather_code",
            "temperature_2m_max",
            "temperature_2m_min",
            "sunrise",
            "sunset",
            "uv_index_max",
            "precipitation_probability_max"
        ),
        @Query("timezone") timezone: String = "GMT"
    ): WeatherInfoDto

    companion object {
        const val BASE_URL = "https://api.open-meteo.com/"
    }
}