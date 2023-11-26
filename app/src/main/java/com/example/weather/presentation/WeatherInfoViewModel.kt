package com.example.weather.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather.core.util.Resource
import com.example.weather.domain.model.LatLng
import com.example.weather.domain.use_case.GetWeatherInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WeatherInfoViewModel(
    private val getWeatherInfo: GetWeatherInfo
) : ViewModel() {

    private val _state = mutableStateOf(WeatherInfoState())
    val state: State<WeatherInfoState> = _state

    private var job: Job? = null

    fun onSearch(latLng: LatLng) {
        job?.cancel()
        job = viewModelScope.launch {
            delay(100L)
            getWeatherInfo(latLng).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            weatherInfo = result.data,
                            isLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(
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