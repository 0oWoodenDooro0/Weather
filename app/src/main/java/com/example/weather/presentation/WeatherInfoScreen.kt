package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.core.Weather
import com.example.weather.domain.model.LatLng
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherInfoScreen(
    modifier: Modifier = Modifier,
    weatherState: () -> WeatherInfoState,
    onSearchWithCity: (LatLng, Int, GPSState) -> Unit,
    selectedIndex: Int,
    onGPSClick: () -> Unit,
    gpsState: GPSState
) {
    Column(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = true) {
            onSearchWithCity(Weather.cityLatLngs[0], 0, GPSState.GPSNotFixed)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = Weather.citys[selectedIndex],
                    onValueChange = {},
                    label = { Text("縣市") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Weather.citys.forEachIndexed { index, _ ->
                        DropdownMenuItem(
                            text = { Text(text = Weather.citys[index]) },
                            onClick = {
                                onSearchWithCity(
                                    Weather.cityLatLngs[index],
                                    index,
                                    GPSState.GPSNotFixed
                                )
                                expanded = false
                            }
                        )
                    }
                }
            }
            IconButton(onClick = {
                onGPSClick()
            }) {
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
        weatherState().weatherInfo?.let { weatherInfo ->
            val current = weatherInfo.current
            val hourly = weatherInfo.hourly
            val daily = weatherInfo.daily
            val hourlyOffset = hourly.time.getHourlyOffset(
                current.time.hourlyToLocalDateTime().truncatedTo(ChronoUnit.HOURS)
            )
            Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                Text(
                    text = "Now",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 20.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${current.temperature_2m.roundToInt()}°",
                            fontSize = 60.sp
                        )
                        Image(
                            painter = painterResource(
                                id = if (current.time.hourlyToLocalDateTime().plusHours(8L)
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
                        fontSize = 16.sp
                    )
                }
                Text(
                    text = "High: ${
                        daily.temperature_2m_max.first().roundToInt()
                    }° Low: ${daily.temperature_2m_min.first().roundToInt()}°"
                )
                Text(
                    text = "Hourly forecast",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 20.sp
                )
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
                        key(hourly.time[index + hourlyOffset]) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${hourly.temperature_2m[index + hourlyOffset].roundToInt()}°",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = if (hourly.precipitation_probability[index + hourlyOffset] < 10) "" else "${hourly.precipitation_probability[index + hourlyOffset]}%",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Image(
                                        painter = painterResource(
                                            id = if (hourly.time[index + hourlyOffset].hourlyToLocalDateTime()
                                                    .isDay()
                                            ) Weather.weatherIcons[hourly.weather_code[index + hourlyOffset]]!!.first else Weather.weatherIcons[hourly.weather_code[index + hourlyOffset]]!!.second
                                        ),
                                        contentDescription = "Weather",
                                        modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp)
                                    )
                                    Text(
                                        text = if (index == 0) "Now" else hourly.time[index + hourlyOffset].hourlyToLocalDateTime()
                                            .formatToHour(),
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "7-day forecast",
                    modifier = Modifier.padding(vertical = 10.dp),
                    fontSize = 20.sp
                )
                LazyColumn {
                    items(count = daily.time.size) { index ->
                        key(daily.time[index]) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = if (index != 0 && index != daily.time.size - 1) RectangleShape else if (index == 0) RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp
                                        ) else RoundedCornerShape(
                                            bottomStart = 10.dp,
                                            bottomEnd = 10.dp
                                        )
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (index == 0) "TODAY" else daily.time[index].dailyToLocalDateTime().dayOfWeek.toString(),
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.weight(2f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (daily.precipitation_probability_max[index] < 10) "" else "${daily.precipitation_probability_max[index]}%",
                                        modifier = Modifier.weight(2f),
                                        textAlign = TextAlign.End,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Image(
                                        painter = painterResource(id = Weather.weatherIcons[daily.weather_code[index]]!!.first),
                                        contentDescription = "Weather",
                                        modifier = Modifier
                                            .weight(1f)
                                            .width(30.dp)
                                            .height(30.dp)
                                    )
                                }
                                Text(
                                    text = "${daily.temperature_2m_max[index].roundToInt()}°/${daily.temperature_2m_min[index].roundToInt()}°",
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 10.dp, top = 10.dp, bottom = 10.dp),
                                    textAlign = TextAlign.End,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun List<String>.getHourlyOffset(currentTime: LocalDateTime): Int {
    this.forEachIndexed { index, time ->
        if (time.hourlyToLocalDateTime().isEqual(currentTime)) {
            return index + 8
        }
    }
    return 0
}

fun String.hourlyToLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
}

fun String.dailyToLocalDateTime(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun LocalDateTime.formatToHour(): String {
    return DateTimeFormatter.ofPattern("h a").format(this)
}

fun LocalDateTime.isDay(): Boolean {
    return this.hour in 6..17
}