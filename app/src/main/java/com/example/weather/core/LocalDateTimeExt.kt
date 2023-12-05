package com.example.weather.core

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun List<String>.getHourlyOffset(currentTime: LocalDateTime): Int {
    this.forEachIndexed { index, time ->
        if (time.minuteToLocalDateTime().isEqual(currentTime)) {
            return index + 8
        }
    }
    return 0
}

fun String.minuteToLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
}

fun String.dailyToLocalDateTime(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun LocalDateTime.formatToHour(): String {
    return DateTimeFormatter.ofPattern("h a").format(this)
}

fun LocalDateTime.formatToMinute(): String {
    return DateTimeFormatter.ofPattern("HH:mm").format(this)
}

fun LocalDateTime.isDay(): Boolean {
    return this.hour in 6..17
}