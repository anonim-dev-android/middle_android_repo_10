package ru.yandex.buggyweatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.yandex.buggyweatherapp.ui.screens.WeatherScreen
import ru.yandex.buggyweatherapp.ui.screens.WeatherViewModel
import ru.yandex.buggyweatherapp.ui.theme.BuggyWeatherAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                Log.d("Main", "ACCESS_FINE_LOCATION is granted")
                start()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                Log.d("Main", "ACCESS_COARSE_LOCATION is granted")
                start()
            }
            else -> {
                Log.e("Main", "No location permissions have been granted")
                Toast.makeText(this, this.getString(R.string.no_location_permission), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()

        enableEdgeToEdge()
        setContent {
            BuggyWeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherScreen(
                        viewModel = weatherViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun checkPermissions() {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocation && !hasCoarseLocation) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            start()
        }
    }

    private fun start() {
        weatherViewModel.startLocationTracking()
        weatherViewModel.initialize()
    }

    override fun onDestroy() {
        weatherViewModel.stopLocationTracking()
        super.onDestroy()
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    BuggyWeatherAppTheme {

        Text("Weather App Preview")
    }
}