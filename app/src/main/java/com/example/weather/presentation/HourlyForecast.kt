package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.core.Weather
import com.example.weather.core.formatToHour
import com.example.weather.core.isDay
import com.example.weather.core.minuteToLocalDateTime
import com.example.weather.domain.model.Hourly
import kotlin.math.roundToInt


fun LazyListScope.hourlyForecast(hourly: Hourly, hourlyOffset: Int) {
    item {
        Text(
            text = "Hourly forecast",
            modifier = Modifier.padding(vertical = 10.dp),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp
        )
    }
    item {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp)
                ),
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
                        if (index == 0) "Now" else hourly.time[hourlyIndex].minuteToLocalDateTime()
                            .formatToHour()
                    )
                }
            }
        }
    }
}

@Composable
fun HourlyForecastItem(
    temperature: String,
    precipitationProbability: String,
    iconId: Int,
    hour: String
) {
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = temperature,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = precipitationProbability,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Image(
            painter = painterResource(iconId),
            contentDescription = "Weather",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
        )
        Text(
            text = hour,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}