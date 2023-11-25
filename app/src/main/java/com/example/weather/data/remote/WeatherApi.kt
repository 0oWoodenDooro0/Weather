package com.example.weather.data.remote

import com.example.weather.data.remote.dto.weather_info.WeatherInfoDto
import com.example.weather.data.remote.dto.weekly_weather_info.WeeklyWeatherInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/api/v1/rest/datastore/F-C0032-001")
    suspend fun getWeatherInfo(
        @Query("Authorization") key: String,
        @Query("locationName") location: String,
        @Query("sort") sort: String = "time",
        @Query("timeFrom") timeFrom: String = ""
    ): WeatherInfoDto

    @GET("/api/v1/rest/datastore/F-D0047-091")
    suspend fun getWeekWeatherInfo(
        @Query("Authorization") key: String,
        @Query("locationName") location: String,
        @Query("elementName") elementName: Array<String> = arrayOf("Wx", "MinT", "MaxT"),
        @Query("sort") sort: String = "time",
        @Query("timeFrom") timeFrom: String = ""
    ): WeeklyWeatherInfoDto

    companion object {
        const val CWA_BASE_URL = "https://opendata.cwa.gov.tw/"
        const val BASE_URL = "https://open-meteo.com/"
    }
}