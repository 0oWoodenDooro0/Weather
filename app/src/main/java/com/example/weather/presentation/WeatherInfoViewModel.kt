package com.example.weather.presentation

import android.location.Geocoder
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.core.Weather
import com.example.weather.core.util.Resource
import com.example.weather.domain.model.LatLng
import com.example.weather.domain.use_case.GetWeatherInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WeatherInfoViewModel(
    private val getWeatherInfo: GetWeatherInfo
) : ViewModel() {

    private val _latlng = MutableStateFlow<LatLng?>(null)

    private val _locationCity = MutableStateFlow<String?>(null)

    private val _selectedIndex = mutableStateOf(0)
    val selectedIndex: State<Int> = _selectedIndex

    private val _gpsState = mutableStateOf<GPSState>(GPSState.NoPremission)
    val gpsState: State<GPSState> = _gpsState

    private val _weatherInfoState = mutableStateOf(WeatherInfoState())
    val weatherInfoState: State<WeatherInfoState> = _weatherInfoState

    private var job: Job? = null

    fun getLocationCityFromLatLng(geocoder: Geocoder, latLng: LatLng) {
        _latlng.value = latLng
        val geocodeListener =
            Geocoder.GeocodeListener { addresses ->
                if (addresses.first().adminArea in Weather.citys) {
                    _locationCity.value = addresses.first().adminArea
                    onSearchWithLatLng(
                        latLng,
                        Weather.citys.indexOf(_locationCity.value),
                        GPSState.GPSFixed
                    )
                }
            }
        if (Build.VERSION.SDK_INT >= 33) {
            geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1, geocodeListener
            )
        } else {
            @Suppress("DEPRECATION") val city =
                geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1
                )?.let { addresses ->
                    addresses.first()?.adminArea
                }
            if (city in Weather.citys) {
                if (_locationCity.value != city) {
                    _locationCity.value = city
                }
                onSearchWithLatLng(
                    latLng,
                    Weather.citys.indexOf(_locationCity.value),
                    GPSState.GPSFixed
                )
            }
        }
    }

    fun changeGPSState(state: GPSState) {
        _gpsState.value = state
    }

    fun onSearchWithLatLng(latLng: LatLng, index: Int, gpsState: GPSState) {
        _selectedIndex.value = index
        job?.cancel()
        job = viewModelScope.launch {
            getWeatherInfo(latLng).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _weatherInfoState.value = _weatherInfoState.value.copy(
                            weatherInfo = result.data,
                            isLoading = false
                        )
                        if (_gpsState.value != GPSState.NoPremission) {
                            changeGPSState(gpsState)
                        }
                    }

                    is Resource.Loading -> {
                        _weatherInfoState.value = _weatherInfoState.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Error -> {
                        _weatherInfoState.value = _weatherInfoState.value.copy(
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    class WeatherInfoViewModelFactory(
        private val getWeatherInfo: GetWeatherInfo
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherInfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return WeatherInfoViewModel(getWeatherInfo) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }
    }

}