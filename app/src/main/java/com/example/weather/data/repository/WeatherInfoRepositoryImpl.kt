package com.example.weather.data.repository

import com.example.weather.core.util.Resource
import com.example.weather.data.remote.WeatherApi
import com.example.weather.domain.model.weather_info.WeatherInfo
import com.example.weather.domain.model.weekly_weather_info.WeeklyWeatherInfo
import com.example.weather.domain.repository.WeatherInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WeatherInfoRepositoryImpl(
    private val api: WeatherApi,
    private val key: String
) : WeatherInfoRepository {

    override fun getWeatherInfo(location: String, timeFrom: String): Flow<Resource<WeatherInfo>> =
        flow {
            emit(Resource.Loading())
            try {
                val remoteWeatherInfo =
                    api.getWeatherInfo(key = key, location = location, timeFrom = timeFrom)
                emit(Resource.Success(remoteWeatherInfo.toWeatherInfo()))
            } catch (e: HttpException) {
                emit(Resource.Error(message = "Oops, something went wrong"))
            } catch (e: IOException) {
                emit(Resource.Error(message = "Couldn't reach server, check your internet connection."))
            }
        }

    override fun getWeeklyWeatherInfo(
        location: String,
        timeFrom: String
    ): Flow<Resource<WeeklyWeatherInfo>> = flow {
        emit(Resource.Loading())
        try {
            val remoteWeeklyWeatherInfo =
                api.getWeekWeatherInfo(key = key, location = location, timeFrom = timeFrom)
            emit(Resource.Success(remoteWeeklyWeatherInfo.toWeeklyWeatherInfo()))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Oops, something went wrong"))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Couldn't reach server, check your internet connection."))
        }
    }

}