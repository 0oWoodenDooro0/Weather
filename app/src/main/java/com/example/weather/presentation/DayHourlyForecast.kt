package com.example.weather.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.core.Weather
import com.example.weather.core.formatToHour
import com.example.weather.core.isDay
import com.example.weather.core.minuteToLocalDateTime
import com.example.weather.domain.model.Hourly
import kotlin.math.roundToInt

@Composable
fun DayHourlyForecast(
    listState: LazyListState,
    hourly: Hourly,
    hourlyOffset: Int,
    isToday: Boolean
) {
    Text(
        text = "Hourly forecast",
        modifier = Modifier.padding(vertical = 10.dp),
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 20.sp
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(10.dp)
            ),
        state = listState,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(count = 25) { index ->
            val hourlyIndex = index + hourlyOffset
            key(hourly.time[hourlyIndex]) {
                HourlyForecastItem(
                    "${hourly.temperature_2m[hourlyIndex].roundToInt()}°",
                    if (hourly.precipitation_probability[hourlyIndex] < 10) "" else "${hourly.precipitation_probability[hourlyIndex]}%",
                    if (hourly.time[hourlyIndex].minuteToLocalDateTime()
                            .isDay()
                    ) Weather.weatherIcons[hourly.weather_code[hourlyIndex]]!!.first else Weather.weatherIcons[hourly.weather_code[hourlyIndex]]!!.second,
                    if (index == 0 && isToday) "Now" else hourly.time[hourlyIndex].minuteToLocalDateTime()
                        .formatToHour()
                )
            }
        }
    }
}