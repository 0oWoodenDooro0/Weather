package com.example.weather

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weather.core.LocationClient
import com.example.weather.domain.model.LatLng
import com.example.weather.presentation.WeatherInfoScreen
import com.example.weather.ui.theme.WeatherTheme
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
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ), 0
        )
        locationClient = LocationClient(applicationContext, this)
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            var latLng by remember { mutableStateOf<LatLng?>(null) }
            var locationCity by remember { mutableStateOf<String?>(null) }
            SideEffect {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb()),
                    navigationBarStyle = SystemBarStyle.auto(Color.Transparent.toArgb(), Color.Transparent.toArgb())
                )
                locationClient.getLastLocation().onEach { currentLocation ->
                    latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                    latLng?.let {
                        val geocoder = Geocoder(applicationContext, Locale.TAIWAN)
                        val geocodeListener = Geocoder.GeocodeListener { addresses ->
                            if (locationCity != addresses.first().adminArea) {
                                locationCity = addresses.first().adminArea
                            }
                        }
                        if (Build.VERSION.SDK_INT >= 33) {
                            geocoder.getFromLocation(it.latitude, it.longitude, 1, geocodeListener)
                        } else {
                            @Suppress("DEPRECATION")
                            val city = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                                ?.let { addresses ->
                                    addresses.first()?.adminArea
                                }
                            if (locationCity != city) {
                                locationCity = city
                            }
                        }
                    }
                }.launchIn(lifecycleScope)
            }
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { contentPadding ->
                        WeatherInfoScreen(
                            modifier = Modifier.padding(contentPadding),
                            application = application as WeatherApp,
                            latLng = latLng,
                            locationCity = locationCity,
                            onGPSClick = {
                                locationClient.getLastLocation().onEach { currentLocation ->
                                    latLng =
                                        LatLng(currentLocation.latitude, currentLocation.longitude)
                                    latLng?.let {
                                        val geocoder = Geocoder(applicationContext, Locale.TAIWAN)
                                        val geocodeListener =
                                            Geocoder.GeocodeListener { addresses ->
                                                if (locationCity != addresses.first().adminArea) {
                                                    locationCity = addresses.first().adminArea
                                                }
                                            }
                                        if (Build.VERSION.SDK_INT >= 33) {
                                            geocoder.getFromLocation(
                                                it.latitude,
                                                it.longitude,
                                                1,
                                                geocodeListener
                                            )
                                        } else {
                                            @Suppress("DEPRECATION")
                                            val city = geocoder.getFromLocation(
                                                it.latitude,
                                                it.longitude,
                                                1
                                            )
                                                ?.let { addresses ->
                                                    addresses.first()?.adminArea
                                                }
                                            if (locationCity != city) {
                                                locationCity = city
                                            }
                                        }
                                    }
                                }.launchIn(lifecycleScope)
                            }
                        )
                    }
                }
            }
        }
    }
}