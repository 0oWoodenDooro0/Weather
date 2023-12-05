package com.example.weather.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DailyForecastItem(
    shape: Shape,
    week: String,
    precipitationProbability: String,
    iconId: Int,
    temperature: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = week,
            modifier = Modifier
                .weight(1.5f)
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = precipitationProbability,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Image(
                painter = painterResource(iconId),
                contentDescription = "Weather",
                modifier = Modifier
                    .weight(1f)
                    .width(30.dp)
                    .height(30.dp)
            )
        }
        Text(
            text = temperature,
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp, top = 10.dp, bottom = 10.dp),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}