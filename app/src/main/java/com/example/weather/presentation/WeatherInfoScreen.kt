package com.example.weather.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weather.R
import com.example.weather.core.Weather
import com.example.weather.core.getHourlyOffset
import com.example.weather.core.minuteToLocalDateTime
import com.example.weather.domain.model.LatLng
import java.time.temporal.ChronoUnit

@Composable
fun WeatherInfoScreen(
    weatherState: () -> WeatherInfoState,
    onSearchWithCity: (LatLng, Int, GPSState) -> Unit,
    selectedIndex: Int,
    onGPSClick: () -> Unit,
    gpsState: GPSState
) {
    Column {
        LaunchedEffect(key1 = true) {
            onSearchWithCity(Weather.cityLatLngs[0], 0, GPSState.GPSNotFixed)
        }
        LazyColumn(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CitySelection(selectedIndex, onSearchWithCity)
                    IconButton(onClick = { onGPSClick() }) {
                        Icon(
                            painter = painterResource(
                                id = when (gpsState) {
                                    GPSState.NoPremission -> R.drawable.baseline_gps_off_24
                                    GPSState.GPSFixed -> R.drawable.baseline_gps_fixed_24
                                    GPSState.GPSNotFixed -> R.drawable.baseline_gps_not_fixed_24
                                }
                            ),
                            contentDescription = "GPS"
                        )
                    }
                }
            }
            if (!weatherState().isLoading){
                weatherState().weatherInfo?.let { weatherInfo ->
                    val current = weatherInfo.current
                    val hourly = weatherInfo.hourly
                    val daily = weatherInfo.daily
                    val hourlyOffset = hourly.time.getHourlyOffset(
                        current.time.minuteToLocalDateTime().truncatedTo(ChronoUnit.HOURS)
                    )
                    currentWeather(
                        current,
                        daily.temperature_2m_max.first(),
                        daily.temperature_2m_min.first()
                    )
                    hourlyForecast(hourly, hourlyOffset)
                    dailyForecastItem(daily)
                    condition(current, daily)
                    item { Spacer(modifier = Modifier.navigationBarsPadding()) }
                }
            }
        }
        AnimatedVisibility(visible = weatherState().isLoading, enter = fadeIn()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}