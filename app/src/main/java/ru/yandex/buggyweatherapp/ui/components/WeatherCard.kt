package ru.yandex.buggyweatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.yandex.buggyweatherapp.R
import ru.yandex.buggyweatherapp.domain.model.WeatherData
import ru.yandex.buggyweatherapp.utils.WeatherIconMapper

@Composable
fun WeatherCard(
    weather: WeatherData,
    cityName: String,
    onFavoriteClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cityName.ifEmpty { weather.cityName },
                    style = MaterialTheme.typography.headlineMedium
                )

                Row {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (weather.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = context.getString(R.string.favorite)
                        )
                    }

                    IconButton(onClick = onRefreshClick) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = context.getString(R.string.refresh)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = context.getString(R.string.temperature, weather.temperature.toString()),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = context.getString(R.string.feels_like, weather.feelsLike.toString()),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = context.getString(R.string.description, weather.description.replaceFirstChar { it.uppercase() }),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = context.getString(R.string.humidity, weather.humidity.toString()),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = context.getString(R.string.wind, weather.windSpeed.toString()),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = context.getString(R.string.sunrise, WeatherIconMapper.formatTimestamp(weather.sunriseTime)),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = context.getString(R.string.sunset, WeatherIconMapper.formatTimestamp(weather.sunsetTime)),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRefreshClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(context.getString(R.string.refresh_weather))
            }
        }
    }
}
