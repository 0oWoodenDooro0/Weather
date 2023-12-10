package com.example.weather.core

import com.example.weather.R
import com.example.weather.domain.model.LatLng

object Weather {
    val citys = listOf(
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
    val weatherIcons = mapOf(
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

    const val WEATHER_PAGE = "weatherPage"
    const val DAILY_FORECAST_PAGE = "dailyForecastPage"
}