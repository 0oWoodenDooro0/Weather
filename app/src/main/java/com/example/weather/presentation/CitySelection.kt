package com.example.weather.presentation

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.weather.core.Weather
import com.example.weather.domain.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelection(selectedIndex: Int, onSearchWithCity: (LatLng, Int, GPSState) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = Weather.citys[selectedIndex],
            onValueChange = {},
            label = { Text("City") },
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
}