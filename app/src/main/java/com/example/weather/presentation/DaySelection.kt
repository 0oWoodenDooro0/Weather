package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.core.Weather
import com.example.weather.core.dailyToLocalDate
import com.example.weather.core.formatToDayOfWeek
import com.example.weather.domain.model.Daily
import kotlin.math.roundToInt

fun LazyListScope.daySelection(selectedTabIndex: Int, daily: Daily, changeTab: (Int) -> Unit) {
    item {
        ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
            daily.time.forEachIndexed { index, day ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { changeTab(index) }
                ) {
                    DaySelectionItem(
                        color = if (selectedTabIndex != index) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primaryContainer,
                        textColor = if (selectedTabIndex != index) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimaryContainer,
                        dayOfWeek = if (index != 0) day.dailyToLocalDate()
                            .formatToDayOfWeek() else "Today",
                        imageId = Weather.weatherIcons[daily.weather_code[index]]!!.first,
                        temperature = "${daily.temperature_2m_max[index].roundToInt()}°/${daily.temperature_2m_min[index].roundToInt()}°"
                    )
                }
            }
        }
    }
}

@Composable
fun DaySelectionItem(
    color: Color,
    textColor: Color,
    dayOfWeek: String,
    imageId: Int,
    temperature: String
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dayOfWeek,
                color = textColor,
                fontSize = 16.sp
            )
            Image(
                painter = painterResource(id = imageId),
                contentDescription = "Weather",
                modifier = Modifier
                    .wrapContentSize()
                    .width(40.dp)
                    .height(40.dp)
            )
            Text(
                text = temperature,
                color = textColor,
                fontSize = 14.sp
            )
        }
    }
}