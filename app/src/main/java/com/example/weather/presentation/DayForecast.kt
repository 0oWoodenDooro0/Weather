package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DayForecast(dayOfWeek: String, temperature: String, imageId: Int) {
    Text(text = dayOfWeek, fontSize = 20.sp)
    Row(verticalAlignment = Alignment.Bottom) {
        Text(text = temperature, fontSize = 52.sp)
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "Weather",
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
        )
    }
}