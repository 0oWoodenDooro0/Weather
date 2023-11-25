package com.example.weather.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.WeatherApp
import com.example.weather.domain.model.weather_info.Location
import com.example.weather.domain.model.weather_info.Parameter
import com.example.weather.domain.model.weather_info.Record
import com.example.weather.domain.model.weather_info.Time
import com.example.weather.domain.model.weather_info.WeatherElement
import com.example.weather.domain.model.weather_info.WeatherInfo
import com.example.weather.domain.model.weekly_weather_info.ElementValue
import com.example.weather.domain.model.weekly_weather_info.WeeklyWeatherInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherInfoScreen(
    modifier: Modifier = Modifier,
    application: WeatherApp,
    weatherViewModel: WeatherInfoViewModel = viewModel(
        factory = WeatherInfoViewModel.WeatherInfoViewModelFactory(
            application.getWeatherInfo,
            application.getWeeklyWeatherInfo
        )
    ),
    locationCity: String? = null
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
        var expanded by remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(0) }
        LaunchedEffect(key1 = locationCity) {
            locationCity?.let {
                if (locationCity in options) {
                    selectedIndex = options.indexOf(locationCity)
                }
            }
        }
        LaunchedEffect(key1 = selectedIndex) {
            weatherViewModel.onSearch(options[selectedIndex])
        }
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
        val weatherState = weatherViewModel.state
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            item {
                weatherState.value.weatherInfo?.record?.locations?.first()?.weatherElements
                    ?.let { weathers ->
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
                            Text(
                                text = "${weathers[0].times.first().parameter.parameterName}",
                                fontSize = 30.sp
                            )
                            Column {
                                Text(
                                    text = "${weathers[3].times.first().parameter.parameterName}",
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "降雨機率: ${weathers[1].times.first().parameter.parameterName}%",
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Text(text = "High: ${weathers[4].times.first().parameter.parameterName}° Low: ${weathers[2].times.first().parameter.parameterName}°")
                        Text(
                            text = "Forecast",
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            fontSize = 20.sp
                        )
                        Card {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                items(count = weathers[0].times.size) { index->
                                    Column(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${weathers[4].times[index].parameter.parameterName}°/${weathers[2].times.first().parameter.parameterName}°",
                                            fontSize = 16.sp
                                        )
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "${weathers[0].times[index].parameter.parameterName}",
                                                modifier = Modifier.padding(10.dp)
                                            )
                                            Text(
                                                text = "${
                                                    weathers[0].times[index].startTime.toLocalDateTime()
                                                        .formatToHour()
                                                } - ${
                                                    weathers[0].times[index].endTime.toLocalDateTime()
                                                        .formatToHour()
                                                }",
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Text(
                            text = "Weekly Forecast",
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            fontSize = 20.sp
                        )
                    }
            }
            weatherState.value.weeklyWeatherInfo?.records?.locations?.first()?.location?.first()?.weatherElement?.let { weeklyWeathers ->
                items(count = weeklyWeathers[0].time.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${
                                weeklyWeathers[0].time[index].startTime.toLocalDateTime()
                                    .formatToDateAndHour()
                            } - ${
                                weeklyWeathers[0].time[index].endTime.toLocalDateTime()
                                    .formatToHour()
                            }"
                        )
                        Text(text = weeklyWeathers[0].time[index].elementValue.first().value)
                        Text(text = "${weeklyWeathers[2].time[index].elementValue.first().value}°/${weeklyWeathers[1].time[index].elementValue.first().value}°")
                    }
                }
            }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Screen() {
    Scaffold {
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
        var expanded by remember { mutableStateOf(true) }
        var selectedIndex by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier.padding(it)
        ) {
            val weathers = WeatherInfo(
                Record(
                    "", listOf(
                        Location(
                            "", listOf(
                                WeatherElement(
                                    "Wx",
                                    listOf(
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "晴時多雲",
                                                parameterUnit = null,
                                                parameterValue = "2"
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        ),
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "晴時多雲",
                                                parameterUnit = null,
                                                parameterValue = "2"
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        )
                                    )
                                ),
                                WeatherElement(
                                    "PoP",
                                    listOf(
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "0",
                                                parameterUnit = "百分比",
                                                parameterValue = null
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        ),
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "0",
                                                parameterUnit = "百分比",
                                                parameterValue = null
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        )
                                    )
                                ),
                                WeatherElement(
                                    "MinT",
                                    listOf(
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "15",
                                                parameterUnit = "C",
                                                parameterValue = null
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        ),
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "15",
                                                parameterUnit = "C",
                                                parameterValue = null
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        )
                                    )
                                ),
                                WeatherElement(
                                    "CI",
                                    listOf(
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "寒冷至稍有寒意",
                                                parameterUnit = null,
                                                parameterValue = null
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        ),
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "寒冷至稍有寒意",
                                                parameterUnit = null,
                                                parameterValue = null
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        )
                                    )
                                ),
                                WeatherElement(
                                    "MaxT",
                                    listOf(
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "20",
                                                parameterUnit = "C",
                                                parameterValue = null
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        ),
                                        Time(
                                            endTime = "2023-11-18 18:00:00",
                                            parameter = Parameter(
                                                parameterName = "20",
                                                parameterUnit = "C",
                                                parameterValue = null
                                            ),
                                            startTime = "2023-11-18 12:00:00"
                                        )
                                    )
                                )
                            )
                        )
                    )
                ), ""
            ).record.locations.first().weatherElements
            val weeklyWeathers = WeeklyWeatherInfo(
                com.example.weather.domain.model.weekly_weather_info.Record(
                    listOf(
                        com.example.weather.domain.model.weekly_weather_info.Location(
                            "",
                            "",
                            listOf(
                                com.example.weather.domain.model.weekly_weather_info.LocationX(
                                    "",
                                    "",
                                    "",
                                    "",
                                    listOf(
                                        com.example.weather.domain.model.weekly_weather_info.WeatherElement(
                                            "",
                                            "",
                                            listOf(
                                                com.example.weather.domain.model.weekly_weather_info.Time(
                                                    listOf(
                                                        ElementValue(
                                                            "",
                                                            "多雲時晴"
                                                        )
                                                    ),
                                                    "2023-11-25 18:00:00",
                                                    "2023-11-25 06:00:00"
                                                )
                                            )
                                        ),
                                        com.example.weather.domain.model.weekly_weather_info.WeatherElement(
                                            "",
                                            "",
                                            listOf(
                                                com.example.weather.domain.model.weekly_weather_info.Time(
                                                    listOf(
                                                        ElementValue(
                                                            "",
                                                            "12"
                                                        )
                                                    ),
                                                    "",
                                                    ""
                                                )
                                            )
                                        ),
                                        com.example.weather.domain.model.weekly_weather_info.WeatherElement(
                                            "",
                                            "",
                                            listOf(
                                                com.example.weather.domain.model.weekly_weather_info.Time(
                                                    listOf(
                                                        ElementValue(
                                                            "",
                                                            "23"
                                                        )
                                                    ),
                                                    "",
                                                    ""
                                                )
                                            )
                                        )
                                    )
                                )
                            ), ""
                        )
                    )
                ), ""
            ).records.locations.first().location.first().weatherElement
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
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            LazyColumn(modifier = Modifier.padding(10.dp)) {
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
                        Text(
                            text = "${weathers[0].times.first().parameter.parameterName}",
                            fontSize = 30.sp
                        )
                        Column {
                            Text(
                                text = "${weathers[3].times.first().parameter.parameterName}",
                                fontSize = 16.sp
                            )
                            Text(
                                text = "降雨機率: ${weathers[1].times.first().parameter.parameterName}%",
                                fontSize = 12.sp
                            )
                        }
                    }
                    Text(text = "High: ${weathers[4].times.first().parameter.parameterName}° Low: ${weathers[2].times.first().parameter.parameterName}°")
                    Text(
                        text = "Forecast",
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        fontSize = 20.sp
                    )
                    Card {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(count = weathers[0].times.size) { index ->
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${weathers[4].times[index].parameter.parameterName}°/${weathers[2].times[index].parameter.parameterName}°",
                                        fontSize = 16.sp
                                    )
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${weathers[0].times[index].parameter.parameterName}",
                                            modifier = Modifier.padding(10.dp)
                                        )
                                        Text(
                                            text = "${
                                                weathers[0].times[index].startTime.toLocalDateTime()
                                                    .formatToHour()
                                            } - ${
                                                weathers[0].times[index].endTime.toLocalDateTime()
                                                    .formatToHour()
                                            }",
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Text(
                        text = "Weekly Forecast",
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        fontSize = 20.sp
                    )
                }
                items(count = weeklyWeathers[0].time.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${
                                weeklyWeathers[0].time[index].startTime.toLocalDateTime()
                                    .formatToDateAndHour()
                            } - ${
                                weeklyWeathers[0].time[index].endTime.toLocalDateTime()
                                    .formatToHour()
                            }"
                        )
                        Text(text = weeklyWeathers[0].time[index].elementValue.first().value)
                        Text(text = "${weeklyWeathers[2].time[index].elementValue.first().value}°/${weeklyWeathers[1].time[index].elementValue.first().value}°")
                    }
                }
            }
        }
    }
}

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

fun LocalDateTime.formatToHour(): String {
    return DateTimeFormatter.ofPattern("h a").format(this)
}

fun LocalDateTime.formatToDateAndHour(): String {
    return DateTimeFormatter.ofPattern("MM/dd h a").format(this)
}