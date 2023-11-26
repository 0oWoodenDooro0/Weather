package com.example.weather.data.repository

import com.example.weather.core.util.Resource
import com.example.weather.data.remote.WeatherApi
import com.example.weather.domain.model.LatLng
import com.example.weather.domain.model.WeatherInfo
import com.example.weather.domain.repository.WeatherInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WeatherInfoRepositoryImpl(
    private val api: WeatherApi
) : WeatherInfoRepository {

    override fun getWeatherInfo(latLng: LatLng): Flow<Resource<WeatherInfo>> =
        flow {
            emit(Resource.Loading())
            try {
                val remoteWeatherInfo =
                    api.getWeatherInfo(latitude = latLng.latitude, longitude = latLng.longitude)
                emit(Resource.Success(remoteWeatherInfo.toWeatherInfo()))
            } catch (e: HttpException) {
                emit(Resource.Error(message = "Oops, something went wrong"))
            } catch (e: IOException) {
                emit(Resource.Error(message = "Couldn't reach server, check your internet connection."))
            }
        }

}