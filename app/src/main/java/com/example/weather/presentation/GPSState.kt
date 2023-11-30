package com.example.weather.presentation

sealed class GPSState {
    object NoPremission : GPSState()
    object GPSFixed : GPSState()
    object GPSNotFixed : GPSState()
}
