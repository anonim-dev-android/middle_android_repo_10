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
            HeaderBlock(
                cityName = cityName.ifEmpty { weather.cityName },
                isFavouriteWeather = weather.isFavorite,
                onFavoriteClick = onFavoriteClick,
                onRefreshClick = onRefreshClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextMedium(
                text = context.getString(R.string.temperature, weather.temperature.toString())
            )
            TextMedium(
                text = context.getString(R.string.feels_like, weather.feelsLike.toString())
            )
            TextMedium(
                text = context.getString(R.string.description, weather.description.replaceFirstChar { it.uppercase() })
            )
            TextMedium(
                text = context.getString(R.string.humidity, weather.humidity.toString())
            )
            TextMedium(
                text = context.getString(R.string.wind, weather.windSpeed.toString())
            )

            Spacer(modifier = Modifier.height(8.dp))

            SunBlock(sunriseTime = weather.sunriseTime, sunsetTime = weather.sunsetTime)

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

@Composable
fun HeaderBlock(
    cityName: String,
    isFavouriteWeather: Boolean,
    onFavoriteClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextHeadline(text = cityName)

        Row {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavouriteWeather) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
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
}

@Composable
fun SunBlock(sunriseTime: Long, sunsetTime: Long) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextSmall(
            text = context.getString(R.string.sunrise, WeatherIconMapper.formatTimestamp(sunriseTime))
        )

        TextSmall(
            text = context.getString(R.string.sunset, WeatherIconMapper.formatTimestamp(sunsetTime))
        )
    }
}
