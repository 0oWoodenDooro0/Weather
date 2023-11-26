package com.example.weather

import android.app.Application
import com.example.weather.data.remote.WeatherApi
import com.example.weather.data.repository.WeatherInfoRepositoryImpl
import com.example.weather.domain.use_case.GetWeatherInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApp : Application() {

    private val weatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
    private val weatherInfoRepository by lazy {
        WeatherInfoRepositoryImpl(weatherApi)
    }
    val getWeatherInfo by lazy { GetWeatherInfo(weatherInfoRepository) }
}