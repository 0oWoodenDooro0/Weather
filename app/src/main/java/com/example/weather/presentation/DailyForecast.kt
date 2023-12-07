package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.weather.core.dailyToLocalDateTime
import com.example.weather.domain.model.Daily
import kotlin.math.roundToInt

fun LazyListScope.dailyForecastItem(daily: Daily){
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
}

@Composable
fun DailyForecastItem(
    shape: Shape,
    week: String,
    precipitationProbability: String,
    iconId: Int,
    temperature: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = week,
            modifier = Modifier
                .weight(1.5f)
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = precipitationProbability,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Image(
                painter = painterResource(iconId),
                contentDescription = "Weather",
                modifier = Modifier
                    .weight(1f)
                    .width(30.dp)
                    .height(30.dp)
            )
        }
        Text(
            text = temperature,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp, top = 10.dp, bottom = 10.dp),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}