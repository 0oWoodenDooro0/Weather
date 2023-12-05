package com.example.weather.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.core.Weather
import com.example.weather.core.dailyToLocalDateTime
import com.example.weather.core.formatToMinute
import com.example.weather.core.getHourlyOffset
import com.example.weather.core.minuteToLocalDateTime
import com.example.weather.domain.model.LatLng
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

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
            weatherState().weatherInfo?.let { weatherInfo ->
                val current = weatherInfo.current
                val hourly = weatherInfo.hourly
                val daily = weatherInfo.daily
                val hourlyOffset = hourly.time.getHourlyOffset(
                    current.time.minuteToLocalDateTime().truncatedTo(ChronoUnit.HOURS)
                )
                item {
                    Text(
                        text = "Now",
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp
                    )
                }
                item {
                    CurrentWeather(
                        current,
                        daily.temperature_2m_max.first(),
                        daily.temperature_2m_min.first()
                    )
                }
                item {
                    Text(
                        text = "Hourly forecast",
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )
                }
                item {
                    HourlyForecast(hourly, hourlyOffset)
                }
                item {
                    Text(
                        text = "7-day forecast",
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )
                }
                items(count = daily.time.size) { index ->
                    key(daily.time[index]) {
                        DailyForecastItem(
                            when (index) {
                                0 -> RoundedCornerShape(
                                    topStart = 10.dp,
                                    topEnd = 10.dp
                                )

                                daily.time.size - 1 -> RoundedCornerShape(
                                    bottomStart = 10.dp,
                                    bottomEnd = 10.dp
                                )

                                else -> RectangleShape
                            },
                            if (index == 0) "TODAY" else daily.time[index].dailyToLocalDateTime().dayOfWeek.toString(),
                            if (daily.precipitation_probability_max[index] < 10) "" else "${daily.precipitation_probability_max[index]}%",
                            Weather.weatherIcons[daily.weather_code[index]]!!.first,
                            "${daily.temperature_2m_max[index].roundToInt()}°/${daily.temperature_2m_min[index].roundToInt()}°"
                        )
                    }
                }
                item {
                    Text(
                        text = "Current conditions",
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                    )
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            ConditionItem(
                                modifier = Modifier.weight(1f),
                                title = "Wind",
                                data = current.wind_speed_10m.roundToInt().toString(),
                                unit = "km/h"
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.up_arrow),
                                    contentDescription = "Wind Direction",
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(60.dp)
                                        .padding(10.dp)
                                        .rotate(current.wind_direction_10m.toFloat() + 180),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            ConditionItem(
                                modifier = Modifier.weight(1f),
                                title = "Humdity",
                                data = current.relative_humidity_2m.toString(),
                                unit = "%"
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.humidity),
                                    contentDescription = "Humidity",
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(60.dp)
                                        .padding(10.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 2.dp, color = MaterialTheme.colorScheme.onSurface)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            ConditionItem(
                                modifier = Modifier.weight(1f),
                                title = "Sunrise",
                                data = daily.sunrise.first().minuteToLocalDateTime().plusHours(8)
                                    .formatToMinute(),
                                unit = ""
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.sunrise),
                                    contentDescription = "Sunrise",
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(60.dp)
                                        .padding(10.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            ConditionItem(
                                modifier = Modifier.weight(1f),
                                title = "Sunset",
                                data = daily.sunset.first().minuteToLocalDateTime().plusHours(8)
                                    .formatToMinute(),
                                unit = ""
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.sunset),
                                    contentDescription = "Sunset",
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(60.dp)
                                        .padding(10.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.navigationBarsPadding()) }
            }
        }
    }
}