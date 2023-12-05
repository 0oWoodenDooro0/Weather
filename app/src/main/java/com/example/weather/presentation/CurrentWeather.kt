package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.core.Weather
import com.example.weather.core.minuteToLocalDateTime
import com.example.weather.core.isDay
import com.example.weather.domain.model.Current
import kotlin.math.roundToInt

@Composable
fun CurrentWeather(current: Current, dailyTemperatureMax: Double, dailyTemperatureMin: Double) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${current.temperature_2m.roundToInt()}°",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 60.sp
                )
                Image(
                    painter = painterResource(
                        id = if (current.time.minuteToLocalDateTime().plusHours(8L)
                                .isDay()
                        ) Weather.weatherIcons[current.weather_code]!!.first else Weather.weatherIcons[current.weather_code]!!.second
                    ),
                    contentDescription = "Weather",
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp)
                )
            }
            Text(
                text = "Feels like ${current.apparent_temperature.roundToInt()}°",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
        }
        Text(
            text = "High: ${dailyTemperatureMax.roundToInt()}° Low: ${dailyTemperatureMin.roundToInt()}°",
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}