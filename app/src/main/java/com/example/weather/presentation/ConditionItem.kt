package com.example.weather.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConditionItem(
    modifier: Modifier = Modifier,
    title: String,
    data: String,
    unit: String,
    image: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        image()
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                modifier = Modifier.padding(5.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp
            )
            Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Bottom) {
                Text(
                    text = data,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                )
                Text(text = unit, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
            }
        }
    }
}