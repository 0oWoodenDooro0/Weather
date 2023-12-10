package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.core.Weather
import com.example.weather.core.dailyToLocalDate
import com.example.weather.core.formatToFullDayOfWeek
import com.example.weather.domain.model.Daily
import kotlin.math.roundToInt

fun LazyListScope.dailyForecast(daily: Daily, navigateToDailyForecast: () -> Unit) {
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
                shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    daily.time.size - 1 -> RoundedCornerShape(
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )

                    else -> RectangleShape
                },
                dayOfWeek = if (index == 0) "Today" else daily.time[index].dailyToLocalDate()
                    .formatToFullDayOfWeek(),
                precipitationProbability = if (daily.precipitation_probability_max[index] < 10) "" else "${daily.precipitation_probability_max[index]}%",
                iconId = Weather.weatherIcons[daily.weather_code[index]]!!.first,
                temperature = "${daily.temperature_2m_max[index].roundToInt()}°/${daily.temperature_2m_min[index].roundToInt()}°",
                navigateToDailyForecast
            )
        }
    }
}

@Composable
fun DailyForecastItem(
    shape: Shape,
    dayOfWeek: String,
    precipitationProbability: String,
    iconId: Int,
    temperature: String,
    navigateToDailyForecast: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape)
            .clickable { navigateToDailyForecast() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dayOfWeek,
            modifier = Modifier
                .weight(1.5f)
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = precipitationProbability,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Image(
            painter = painterResource(iconId),
            contentDescription = "Weather",
            modifier = Modifier
                .wrapContentSize()
                .width(40.dp)
                .height(40.dp)
        )
        Text(
            text = temperature,
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}