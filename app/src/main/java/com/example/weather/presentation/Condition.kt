package com.example.weather.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.core.formatToMinute
import com.example.weather.core.minuteToLocalDateTime
import com.example.weather.domain.model.Current
import com.example.weather.domain.model.Daily
import kotlin.math.roundToInt

fun LazyListScope.condition(current: Current, daily: Daily) {
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
            Divider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
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
}

@Composable
fun ConditionItem(
    modifier: Modifier = Modifier,
    title: String,
    data: String,
    unit: String,
    image: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        image()
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                modifier = Modifier.padding(5.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Bottom) {
                Text(
                    text = data,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                )
                Text(text = unit, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
            }
        }
    }
}