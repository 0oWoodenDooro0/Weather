package com.example.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather.domain.model.LatLng
import com.example.weather.presentation.GPSState
import com.example.weather.presentation.WeatherInfoScreen
import com.example.weather.presentation.WeatherInfoViewModel
import com.example.weather.ui.theme.WeatherTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale


class MainActivity : ComponentActivity() {

    private lateinit var client: FusedLocationProviderClient
    private lateinit var viewModel: WeatherInfoViewModel
    private lateinit var geocoder: Geocoder

    @SuppressLint("MissingPermission", "UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = LocationServices.getFusedLocationProviderClient(applicationContext)
        setContent {
            viewModel =
                viewModel(factory = WeatherInfoViewModel.WeatherInfoViewModelFactory((application as WeatherApp).getWeatherInfo))

            geocoder = Geocoder(applicationContext, Locale.TAIWAN)
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                        viewModel.getLocationCityFromLatLng(
                            geocoder,
                            LatLng(it.latitude, it.longitude)
                        )
                    }
                }
            }

            val requestPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    permissions.forEach { permission ->
                        if (permission.key == Manifest.permission.ACCESS_COARSE_LOCATION) {
                            if (permission.value) {
                                viewModel.changeGPSState(GPSState.GPSNotFixed)
                                getLastLocation(locationCallback)
                            }
                        }
                    }
                }
            )

            SideEffect {
                enableEdgeToEdge()
                getLocation(locationCallback, requestPermissionLauncher)
            }

            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
                ) {
                    Scaffold {
                        WeatherInfoScreen(
                            weatherState = { viewModel.weatherInfoState.value },
                            onSearchWithCity = viewModel::onSearchWithLatLng,
                            selectedIndex = viewModel.selectedIndex.value,
                            onGPSClick = {
                                getLocation(locationCallback, requestPermissionLauncher)
                            },
                            gpsState = viewModel.gpsState.value
                        )
                    }
                }
            }
        }
    }

    fun getLocation(
        locationCallback: LocationCallback,
        requestPermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
    ) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                viewModel.changeGPSState(GPSState.GPSNotFixed)
                getLastLocation(locationCallback)
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData(locationCallback: LocationCallback) {
        val request =
            LocationRequest.Builder(0)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMaxUpdates(1)
                .build()

        client.requestLocationUpdates(
            request,
            locationCallback,
            Looper.myLooper()
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(locationCallback: LocationCallback) {
        client.lastLocation.addOnCompleteListener(this) { task ->
            val location: Location? = task.result
            if (location == null) {
                requestNewLocationData(locationCallback)
            } else {
                viewModel.getLocationCityFromLatLng(
                    geocoder,
                    LatLng(location.latitude, location.longitude)
                )
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}
