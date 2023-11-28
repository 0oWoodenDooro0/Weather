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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.core.Weather
import com.example.weather.domain.model.Current
import com.example.weather.domain.model.Daily
import com.example.weather.domain.model.Hourly
import com.example.weather.domain.model.LatLng
import com.example.weather.domain.model.WeatherInfo
import com.example.weather.ui.theme.WeatherTheme
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
    onSearch: (LatLng) -> Unit,
    latLng: LatLng?,
    locationCity: String?,
    onGPSClick: () -> Unit
) {
    Column(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(0) }
        var gpsFixed by remember { mutableStateOf(true) }
        LaunchedEffect(key1 = locationCity) {
            locationCity?.let {
                if (locationCity in Weather.citys) {
                    selectedIndex = Weather.citys.indexOf(locationCity)
                }
            }
            latLng?.let { onSearch(it) }
        }
        LaunchedEffect(key1 = selectedIndex) {
            onSearch(Weather.cityLatLngs[selectedIndex])
            gpsFixed = false
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
                                selectedIndex = index
                                expanded = false
                            }
                        )
                    }
                }
            }
            IconButton(onClick = {
                onGPSClick()
                gpsFixed = true
            }) {
                Icon(
                    painter = painterResource(
                        id = if (latLng == null) {
                            R.drawable.baseline_gps_off_24
                        } else if (gpsFixed) {
                            R.drawable.baseline_gps_fixed_24
                        } else {
                            R.drawable.baseline_gps_not_fixed_24
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

@Preview
@Composable
fun Pre() {
    WeatherTheme {
        Surface {
            WeatherInfoScreen(weatherState = {
                return@WeatherInfoScreen WeatherInfoState(
                    WeatherInfo(
                        current = Current(
                            apparent_temperature = 17.8,
                            is_day = 0,
                            relative_humidity_2m = 80,
                            surface_pressure = 1021.4,
                            temperature_2m = 19.1,
                            weather_code = 2,
                            wind_direction_10m = 81,
                            wind_speed_10m = 22.6,
                            time = "2023-11-25T15:00"
                        ), daily = Daily(
                            temperature_2m_max = listOf(
                                20.4,
                                22.2,
                                23.1,
                                21.4,
                                26.6,
                                24.0,
                                22.2
                            ),
                            temperature_2m_min = listOf(
                                19.0,
                                16.6,
                                16.2,
                                18.2,
                                15.8,
                                17.7,
                                19.8
                            ),
                            time = listOf(
                                "2023-11-25",
                                "2023-11-26",
                                "2023-11-27",
                                "2023-11-28",
                                "2023-11-29",
                                "2023-11-30",
                                "2023-12-01"
                            ),
                            precipitation_probability_max = listOf(
                                32,
                                35,
                                0,
                                0,
                                18,
                                26,
                                39
                            ),
                            uv_index_max = listOf(
                                3.25,
                                1.3,
                                5.45,
                                5.45,
                                3.65,
                                0.8,
                                1.3
                            ),
                            weather_code = listOf(
                                3,
                                2,
                                1,
                                1,
                                2,
                                3,
                                3
                            )
                        ), hourly = Hourly(
                            is_day = listOf(
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1
                            ),
                            precipitation_probability = listOf(
                                3,
                                2,
                                1,
                                0,
                                1,
                                2,
                                3,
                                2,
                                1,
                                0,
                                1,
                                2,
                                3,
                                4,
                                5,
                                6,
                                6,
                                6,
                                6,
                                13,
                                19,
                                26,
                                29,
                                32,
                                35,
                                28,
                                20,
                                13,
                                13,
                                13,
                                13,
                                9,
                                4,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                2,
                                3,
                                7,
                                12,
                                16,
                                17,
                                18,
                                19,
                                17,
                                15,
                                13,
                                15,
                                17,
                                19,
                                21,
                                24,
                                26,
                                23,
                                19,
                                16,
                                17,
                                18,
                                19,
                                19,
                                19,
                                19,
                                18,
                                17,
                                16,
                                17,
                                18,
                                19,
                                19,
                                19,
                                19,
                                17,
                                15,
                                13,
                                14,
                                15,
                                16,
                                20,
                                25,
                                29,
                                27,
                                25,
                                23,
                                23,
                                23,
                                23,
                                28,
                                34,
                                39,
                                39,
                                39
                            ),
                            temperature_2m = listOf(
                                19.4,
                                19.9,
                                20.0,
                                20.0,
                                20.1,
                                20.2,
                                20.4,
                                19.9,
                                19.5,
                                19.2,
                                19.1,
                                19.2,
                                19.3,
                                19.4,
                                19.4,
                                19.4,
                                19.4,
                                19.3,
                                19.3,
                                19.1,
                                19.1,
                                19.0,
                                19.0,
                                19.1,
                                20.2,
                                21.4,
                                21.8,
                                22.2,
                                22.2,
                                22.2,
                                21.9,
                                21.5,
                                20.9,
                                20.0,
                                19.5,
                                19.0,
                                18.7,
                                18.4,
                                18.1,
                                17.9,
                                17.5,
                                17.0,
                                16.6,
                                17.5,
                                17.3,
                                17.1,
                                17.0,
                                17.1,
                                18.5,
                                19.9,
                                21.6,
                                22.8,
                                23.1,
                                23.0,
                                22.6,
                                22.0,
                                21.1,
                                19.8,
                                18.9,
                                18.1,
                                17.6,
                                17.2,
                                17.0,
                                16.6,
                                16.3,
                                16.2,
                                16.3,
                                17.1,
                                17.0,
                                16.6,
                                16.2,
                                16.3,
                                18.4,
                                19.9,
                                20.5,
                                20.9,
                                21.0,
                                20.9,
                                20.6,
                                21.4,
                                20.4,
                                19.2,
                                18.4,
                                18.2,
                                18.3,
                                18.3,
                                18.4,
                                18.6,
                                18.7,
                                18.8,
                                18.9,
                                18.7,
                                18.4,
                                18.5,
                                19.0,
                                19.7,
                                20.8,
                                22.3,
                                24.1,
                                25.5,
                                26.3,
                                26.6,
                                26.4,
                                25.5,
                                24.0,
                                22.7,
                                22.0,
                                21.5,
                                20.9,
                                20.1,
                                19.3,
                                18.6,
                                18.0,
                                17.4,
                                16.9,
                                16.4,
                                15.9,
                                15.8,
                                16.1,
                                16.7,
                                17.7,
                                19.8,
                                22.3,
                                24.0,
                                23.9,
                                23.0,
                                22.2,
                                21.9,
                                21.2,
                                20.6,
                                20.3,
                                20.3,
                                20.2,
                                20.1,
                                20.0,
                                19.8,
                                19.7,
                                19.5,
                                19.4,
                                19.3,
                                19.2,
                                19.2,
                                19.3,
                                19.5,
                                19.8,
                                20.3,
                                21.0,
                                21.5,
                                21.9,
                                22.2,
                                22.2,
                                21.9,
                                21.3,
                                20.8,
                                20.7,
                                20.6,
                                20.7,
                                20.8,
                                21.0,
                                21.2,
                                21.2,
                                21.2,
                                21.1,
                                20.9,
                                20.7,
                                20.6,
                                20.6,
                                20.9
                            ),
                            time = listOf(
                                "2023-11-25T00:00",
                                "2023-11-25T01:00",
                                "2023-11-25T02:00",
                                "2023-11-25T03:00",
                                "2023-11-25T04:00",
                                "2023-11-25T05:00",
                                "2023-11-25T06:00",
                                "2023-11-25T07:00",
                                "2023-11-25T08:00",
                                "2023-11-25T09:00",
                                "2023-11-25T10:00",
                                "2023-11-25T11:00",
                                "2023-11-25T12:00",
                                "2023-11-25T13:00",
                                "2023-11-25T14:00",
                                "2023-11-25T15:00",
                                "2023-11-25T16:00",
                                "2023-11-25T17:00",
                                "2023-11-25T18:00",
                                "2023-11-25T19:00",
                                "2023-11-25T20:00",
                                "2023-11-25T21:00",
                                "2023-11-25T22:00",
                                "2023-11-25T23:00",
                                "2023-11-26T00:00",
                                "2023-11-26T01:00",
                                "2023-11-26T02:00",
                                "2023-11-26T03:00",
                                "2023-11-26T04:00",
                                "2023-11-26T05:00",
                                "2023-11-26T06:00",
                                "2023-11-26T07:00",
                                "2023-11-26T08:00",
                                "2023-11-26T09:00",
                                "2023-11-26T10:00",
                                "2023-11-26T11:00",
                                "2023-11-26T12:00",
                                "2023-11-26T13:00",
                                "2023-11-26T14:00",
                                "2023-11-26T15:00",
                                "2023-11-26T16:00",
                                "2023-11-26T17:00",
                                "2023-11-26T18:00",
                                "2023-11-26T19:00",
                                "2023-11-26T20:00",
                                "2023-11-26T21:00",
                                "2023-11-26T22:00",
                                "2023-11-26T23:00",
                                "2023-11-27T00:00",
                                "2023-11-27T01:00",
                                "2023-11-27T02:00",
                                "2023-11-27T03:00",
                                "2023-11-27T04:00",
                                "2023-11-27T05:00",
                                "2023-11-27T06:00",
                                "2023-11-27T07:00",
                                "2023-11-27T08:00",
                                "2023-11-27T09:00",
                                "2023-11-27T10:00",
                                "2023-11-27T11:00",
                                "2023-11-27T12:00",
                                "2023-11-27T13:00",
                                "2023-11-27T14:00",
                                "2023-11-27T15:00",
                                "2023-11-27T16:00",
                                "2023-11-27T17:00",
                                "2023-11-27T18:00",
                                "2023-11-27T19:00",
                                "2023-11-27T20:00",
                                "2023-11-27T21:00",
                                "2023-11-27T22:00",
                                "2023-11-27T23:00",
                                "2023-11-28T00:00",
                                "2023-11-28T01:00",
                                "2023-11-28T02:00",
                                "2023-11-28T03:00",
                                "2023-11-28T04:00",
                                "2023-11-28T05:00",
                                "2023-11-28T06:00",
                                "2023-11-28T07:00",
                                "2023-11-28T08:00",
                                "2023-11-28T09:00",
                                "2023-11-28T10:00",
                                "2023-11-28T11:00",
                                "2023-11-28T12:00",
                                "2023-11-28T13:00",
                                "2023-11-28T14:00",
                                "2023-11-28T15:00",
                                "2023-11-28T16:00",
                                "2023-11-28T17:00",
                                "2023-11-28T18:00",
                                "2023-11-28T19:00",
                                "2023-11-28T20:00",
                                "2023-11-28T21:00",
                                "2023-11-28T22:00",
                                "2023-11-28T23:00",
                                "2023-11-29T00:00",
                                "2023-11-29T01:00",
                                "2023-11-29T02:00",
                                "2023-11-29T03:00",
                                "2023-11-29T04:00",
                                "2023-11-29T05:00",
                                "2023-11-29T06:00",
                                "2023-11-29T07:00",
                                "2023-11-29T08:00",
                                "2023-11-29T09:00",
                                "2023-11-29T10:00",
                                "2023-11-29T11:00",
                                "2023-11-29T12:00",
                                "2023-11-29T13:00",
                                "2023-11-29T14:00",
                                "2023-11-29T15:00",
                                "2023-11-29T16:00",
                                "2023-11-29T17:00",
                                "2023-11-29T18:00",
                                "2023-11-29T19:00",
                                "2023-11-29T20:00",
                                "2023-11-29T21:00",
                                "2023-11-29T22:00",
                                "2023-11-29T23:00",
                                "2023-11-30T00:00",
                                "2023-11-30T01:00",
                                "2023-11-30T02:00",
                                "2023-11-30T03:00",
                                "2023-11-30T04:00",
                                "2023-11-30T05:00",
                                "2023-11-30T06:00",
                                "2023-11-30T07:00",
                                "2023-11-30T08:00",
                                "2023-11-30T09:00",
                                "2023-11-30T10:00",
                                "2023-11-30T11:00",
                                "2023-11-30T12:00",
                                "2023-11-30T13:00",
                                "2023-11-30T14:00",
                                "2023-11-30T15:00",
                                "2023-11-30T16:00",
                                "2023-11-30T17:00",
                                "2023-11-30T18:00",
                                "2023-11-30T19:00",
                                "2023-11-30T20:00",
                                "2023-11-30T21:00",
                                "2023-11-30T22:00",
                                "2023-11-30T23:00",
                                "2023-12-01T00:00",
                                "2023-12-01T01:00",
                                "2023-12-01T02:00",
                                "2023-12-01T03:00",
                                "2023-12-01T04:00",
                                "2023-12-01T05:00",
                                "2023-12-01T06:00",
                                "2023-12-01T07:00",
                                "2023-12-01T08:00",
                                "2023-12-01T09:00",
                                "2023-12-01T10:00",
                                "2023-12-01T11:00",
                                "2023-12-01T12:00",
                                "2023-12-01T13:00",
                                "2023-12-01T14:00",
                                "2023-12-01T15:00",
                                "2023-12-01T16:00",
                                "2023-12-01T17:00",
                                "2023-12-01T18:00",
                                "2023-12-01T19:00",
                                "2023-12-01T20:00",
                                "2023-12-01T21:00",
                                "2023-12-01T22:00",
                                "2023-12-01T23:00"
                            ),
                            weather_code = listOf(
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                2,
                                2,
                                2,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                2,
                                2,
                                2,
                                2,
                                1,
                                2,
                                1,
                                2,
                                2,
                                2,
                                2,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                2,
                                2,
                                2,
                                2,
                                2,
                                2,
                                0,
                                0,
                                0,
                                1,
                                1,
                                1,
                                1,
                                1,
                                1,
                                2,
                                2,
                                2,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                2,
                                2,
                                2,
                                2,
                                2,
                                2,
                                3,
                                3,
                                3,
                                3,
                                3,
                                3,
                                2,
                                2,
                                2,
                                3,
                                3
                            )
                        ),
                        latitude = 0.0,
                        longitude = 0.0
                    ), false
                )
            }, onSearch = {}, latLng = null, locationCity = null, onGPSClick = {})
        }
    }
}