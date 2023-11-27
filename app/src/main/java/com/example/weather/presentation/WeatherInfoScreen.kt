package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.R
import com.example.weather.WeatherApp
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
    application: WeatherApp,
    weatherViewModel: WeatherInfoViewModel = viewModel(
        factory = WeatherInfoViewModel.WeatherInfoViewModelFactory(
            application.getWeatherInfo
        )
    ),
    latLng: LatLng?,
    locationCity: String?,
    onGPSClick: () -> Unit
) {
    Column(modifier = modifier) {
        val options = listOf(
            "臺北市",
            "新北市",
            "桃園市",
            "臺中市",
            "臺南市",
            "高雄市",
            "基隆市",
            "新竹縣",
            "新竹市",
            "苗栗縣",
            "彰化縣",
            "南投縣",
            "雲林縣",
            "嘉義縣",
            "嘉義市",
            "屏東縣",
            "宜蘭縣",
            "花蓮縣",
            "臺東縣",
            "澎湖縣",
            "金門縣",
            "連江縣"
        )
        val icons = mapOf(
            0 to Pair(R.drawable.clear_day, R.drawable.clear_night),
            1 to Pair(R.drawable.mainly_clear_day, R.drawable.mainly_clear_night),
            2 to Pair(R.drawable.partly_cloudly_day, R.drawable.partly_cloudly_night),
            3 to Pair(R.drawable.overcast_day, R.drawable.overcast_night),
            45 to Pair(R.drawable.fog, R.drawable.fog),
            48 to Pair(R.drawable.fog, R.drawable.fog),
            51 to Pair(R.drawable.drizzle, R.drawable.drizzle),
            53 to Pair(R.drawable.drizzle, R.drawable.drizzle),
            55 to Pair(R.drawable.drizzle, R.drawable.drizzle),
            56 to Pair(R.drawable.freezing_rain, R.drawable.freezing_rain),
            57 to Pair(R.drawable.freezing_rain, R.drawable.freezing_rain),
            61 to Pair(R.drawable.rain, R.drawable.rain),
            63 to Pair(R.drawable.rain, R.drawable.rain),
            65 to Pair(R.drawable.rain, R.drawable.rain),
            66 to Pair(R.drawable.freezing_rain, R.drawable.freezing_rain),
            67 to Pair(R.drawable.freezing_rain, R.drawable.freezing_rain),
            71 to Pair(R.drawable.snow_fall, R.drawable.snow_fall),
            73 to Pair(R.drawable.snow_fall, R.drawable.snow_fall),
            75 to Pair(R.drawable.snow_fall, R.drawable.snow_fall),
            77 to Pair(R.drawable.snow_grains, R.drawable.snow_grains),
            80 to Pair(R.drawable.rain_shower, R.drawable.rain_shower),
            81 to Pair(R.drawable.rain_shower, R.drawable.rain_shower),
            82 to Pair(R.drawable.heavy_rain_shower, R.drawable.heavy_rain_shower),
            85 to Pair(R.drawable.snow_shower, R.drawable.snow_shower),
            86 to Pair(R.drawable.snow_shower, R.drawable.snow_shower)
        )
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(0) }
        var gpsFixed by remember { mutableStateOf(true) }
        LaunchedEffect(key1 = locationCity) {
            locationCity?.let {
                if (locationCity in options) {
                    selectedIndex = options.indexOf(locationCity)
                }
            }
            latLng?.let { weatherViewModel.onSearch(it) }
        }
        LaunchedEffect(key1 = selectedIndex) {
            val cityLatLngs = listOf(
                LatLng(25.032271975394007, 121.5665022632825),
                LatLng(25.063657710887785, 121.45910629315827),
                LatLng(24.993551842079757, 121.29974453103846),
                LatLng(24.16159627028736, 120.67541651012831),
                LatLng(22.999720315706572, 120.22747249405285),
                LatLng(22.62483578470256, 120.30193708199877),
                LatLng(25.1256261061146, 121.74129458904989),
                LatLng(24.694754437319816, 121.15574071515405),
                LatLng(24.81488194293492, 120.97119733196254),
                LatLng(24.465956080693058, 120.91341660006931),
                LatLng(23.96355250170825, 120.4731148941611),
                LatLng(23.82418346589851, 120.96967690656342),
                LatLng(23.7100427644695, 120.42923266980927),
                LatLng(23.452733408294687, 120.25571165608504),
                LatLng(23.48162272930483, 120.4518668568933),
                LatLng(22.552790927535224, 120.65264820569594),
                LatLng(24.6017711500144, 121.64635438249279),
                LatLng(23.881547901978003, 121.41770489771218),
                LatLng(23.006770530481276, 121.02801270851685),
                LatLng(23.573039324912937, 119.57888539440505),
                LatLng(24.44721326055131, 118.37354654698757),
                LatLng(26.16141282202582, 119.95025445840824),
            )
            weatherViewModel.onSearch(cityLatLngs[selectedIndex])
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
                    value = options[selectedIndex],
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
                    options.forEachIndexed { index, _ ->
                        DropdownMenuItem(
                            text = { Text(text = options[index]) },
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
        val weatherState = weatherViewModel.state
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            weatherState.value.weatherInfo?.let { weatherInfo ->
                val current = weatherInfo.current
                val hourly = weatherInfo.hourly
                val daily = weatherInfo.daily
                val hourlyOffset = hourly.time.getHourlyOffset(
                    current.time.hourlyToLocalDateTime().truncatedTo(ChronoUnit.HOURS)
                )
                item {
                    Text(
                        text = "Now",
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        fontSize = 20.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${current.temperature_2m.roundToInt()}°",
                                fontSize = 30.sp
                            )
                            Image(
                                painter = painterResource(
                                    id = if (current.time.hourlyToLocalDateTime().plusHours(8L)
                                            .isDay()
                                    ) icons[current.weather_code]!!.first else icons[current.weather_code]!!.second
                                ),
                                contentDescription = "Weather",
                                modifier = Modifier
                                    .height(70.dp)
                                    .width(70.dp)
                            )
                        }
                        Text(
                            text = "體感溫度: ${current.apparent_temperature.roundToInt()}°",
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
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        fontSize = 20.sp
                    )
                    Card {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(count = 25) { index ->
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${hourly.temperature_2m[index + hourlyOffset].roundToInt()}°",
                                        fontSize = 14.sp
                                    )
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = if (hourly.precipitation_probability[index + hourlyOffset] < 10) "" else "${hourly.precipitation_probability[index]}%",
                                            fontSize = 12.sp
                                        )
                                        Image(
                                            painter = painterResource(
                                                id = if (hourly.time[index + hourlyOffset].hourlyToLocalDateTime()
                                                        .isDay()
                                                ) icons[hourly.weather_code[index + hourlyOffset]]!!.first else icons[hourly.weather_code[index + hourlyOffset]]!!.second
                                            ),
                                            contentDescription = "Weather",
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(40.dp)
                                        )
                                        Text(
                                            text = if (index == 0) "Now" else hourly.time[index + hourlyOffset].hourlyToLocalDateTime()
                                                .formatToHour(),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        text = "7-day forecast",
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        fontSize = 20.sp
                    )
                }
                items(count = daily.time.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (index == 0) "TODAY" else daily.time[index].dailyToLocalDateTime().dayOfWeek.toString(),
                            modifier = Modifier.weight(1.5f)
                        )
                        Row(
                            modifier = Modifier.weight(2f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (daily.precipitation_probability_max[index] < 10) "" else "${daily.precipitation_probability_max[index]}%",
                                modifier = Modifier.weight(2f),
                                textAlign = TextAlign.End
                            )
                            Image(
                                painter = painterResource(id = icons[daily.weather_code[index]]!!.first),
                                contentDescription = "Weather",
                                modifier = Modifier
                                    .weight(1f)
                                    .width(30.dp)
                                    .height(30.dp)
                            )
                        }
                        Text(
                            text = "${daily.temperature_2m_max[index].roundToInt()}°/${daily.temperature_2m_min[index].roundToInt()}°",
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
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