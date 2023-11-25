package com.example.weather

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weather.core.DefaultLocationClient
import com.example.weather.core.LocationClient
import com.example.weather.presentation.WeatherInfoScreen
import com.example.weather.ui.theme.WeatherTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var locationClient: LocationClient

    @SuppressLint("CoroutineCreationDuringComposition", "FlowOperatorInvokedInComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            var locationCity by remember { mutableStateOf("") }
            locationClient.getLocationUpdates(10000L)
                .catch { e -> e.printStackTrace() }
                .onEach { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val geocoder = Geocoder(this, Locale.TAIWAN)
                    val geocodeListener = Geocoder.GeocodeListener { addresses ->
                        if (locationCity != addresses.first().adminArea) {
                            locationCity = addresses.first().adminArea
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 33) {
                        geocoder.getFromLocation(latitude, longitude, 1, geocodeListener)
                    } else {
                        val city =
                            geocoder.getFromLocation(latitude, longitude, 1)?.let { addresses ->
                                addresses.first()?.adminArea ?: ""
                            } ?: ""
                        if (locationCity != city) {
                            locationCity = city
                        }
                    }
                }.launchIn(lifecycleScope)
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { contentPadding ->
                        WeatherInfoScreen(
                            modifier = Modifier.padding(contentPadding),
                            application = application as WeatherApp,
                            locationCity = locationCity
                        )
                    }
                }
            }
        }
    }
}