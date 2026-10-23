package ru.yandex.buggyweatherapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.yandex.buggyweatherapp.R
import ru.yandex.buggyweatherapp.ui.components.WeatherCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {

    DisposableEffect(Unit) {
        viewModel.initialize()
        onDispose {
            
        }
    }

    val context = LocalContext.current
    val uiState by viewModel.weatherUiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text(context.getString(R.string.search_city)) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { viewModel.searchWeatherByCity(searchText) }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = context.getString(R.string.search)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { 
                viewModel.searchWeatherByCity(searchText) 
            })
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading && uiState.weatherData == null) {
            Text(context.getString(R.string.loading))
        }


        uiState.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        uiState.weatherData?.let { weather ->
            WeatherCard(
                weather = weather,
                cityName = uiState.cityName ?: "",
                onFavoriteClick = { viewModel.toggleFavorite() },
                onRefreshClick = { viewModel.fetchCurrentLocationWeather() }
            )
        }
    }
}
