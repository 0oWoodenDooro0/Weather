package com.example.weather.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weather.R
import com.example.weather.core.Weather
import com.example.weather.core.dailyToLocalDate
import com.example.weather.core.formatToDayOfWeekAndDate
import com.example.weather.core.getHourlyOffset
import com.example.weather.core.minuteToLocalDateTime
import com.example.weather.domain.model.Daily
import com.example.weather.domain.model.Hourly
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyForecastScreen(navigatBack: () -> Unit, weatherState: () -> WeatherInfoState) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "7-day forecast") },
                navigationIcon = {
                    IconButton(onClick = navigatBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "navigate back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            weatherState().weatherInfo?.let { weatherInfo ->
                var selectedTabIndex by remember { mutableIntStateOf(0) }
                val current = weatherInfo.current
                val hourly = weatherInfo.hourly
                val daily = weatherInfo.daily
                val dayHourlyState = rememberLazyListState()
                val scope = rememberCoroutineScope()
                LazyColumn {
                    daySelection(
                        selectedTabIndex,
                        daily,
                        changeTab = {
                            selectedTabIndex = it
                            scope.launch {
                                dayHourlyState.scrollToItem(index = if (it == 0) 0 else 7)
                            }
                        }
                    )
                    dayForecastDetail(
                        selectedTabIndex,
                        daily,
                        dayHourlyState,
                        hourly,
                        currentTime = current.time.minuteToLocalDateTime()
                    )
                }
            }
        }
    }
}

fun LazyListScope.dayForecastDetail(
    selectedTabIndex: Int,
    daily: Daily,
    dayHourlyState: LazyListState,
    hourly: Hourly,
    currentTime: LocalDateTime
) {
    item {
        Column(modifier = Modifier.padding(20.dp)) {
            DayForecast(
                dayOfWeek = if (selectedTabIndex != 0) daily.time[selectedTabIndex].dailyToLocalDate()
                    .formatToDayOfWeekAndDate() else "Today",
                temperature = "${daily.temperature_2m_max[selectedTabIndex].roundToInt()}°/${daily.temperature_2m_min[selectedTabIndex].roundToInt()}°",
                imageId = Weather.weatherIcons[daily.weather_code[selectedTabIndex]]!!.first
            )
            DayHourlyForecast(
                listState = dayHourlyState,
                hourly = hourly,
                hourlyOffset = if (selectedTabIndex != 0) hourly.time.getHourlyOffset(daily.time[selectedTabIndex].dailyToLocalDate()) else hourly.time.getHourlyOffset(
                    currentTime.truncatedTo(ChronoUnit.HOURS)
                ),
                isToday = selectedTabIndex == 0
            )
            DayCondition(daily, selectedTabIndex)
        }
    }
}