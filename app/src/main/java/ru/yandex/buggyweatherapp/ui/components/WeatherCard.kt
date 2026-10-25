package ru.yandex.buggyweatherapp.ui.components

import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ru.yandex.buggyweatherapp.R
import ru.yandex.buggyweatherapp.domain.model.Weather
import ru.yandex.buggyweatherapp.utils.ImageLoader
import ru.yandex.buggyweatherapp.utils.WeatherIconMapper

@Composable
fun WeatherCard(
    weather: Weather,
    cityName: String,
    onFavoriteClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HeaderBlock(
                cityName = if (cityName.isEmpty()) weather.data.cityName else "$cityName, ${weather.data.cityName}",
                isFavouriteWeather = weather.isFavorite,
                onFavoriteClick = onFavoriteClick,
                onRefreshClick = onRefreshClick
            )

            WeatherImageBlock(weather)
            TextLarge(
                text = weather.data.description.replaceFirstChar { it.uppercase() }
            )

            WeatherDataBlock(weather)
            SunBlock(sunriseTime = weather.data.sunriseTime, sunsetTime = weather.data.sunsetTime)
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
fun WeatherImageBlock(weather: Weather) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AndroidView(
            factory = { context -> ImageView(context) },
            modifier = Modifier.size(50.dp)
        ) {
            ImageLoader.loadInto(
                WeatherIconMapper.getPngLink(weather.data.icon),
                it
            )
        }

        TextHeadline(
            text = context.getString(R.string.temperature_celsium, weather.data.temperature)
        )
    }

}

@Composable
fun WeatherDataBlock(weather: Weather) {
    val context = LocalContext.current

    Column {
        TextMedium(
            text = context.getString(R.string.feels_like, weather.data.feelsLike)
        )
        TextMedium(
            text = context.getString(R.string.min_max, weather.data.minTemp, weather.data.minTemp)
        )
        TextMedium(
            text = context.getString(R.string.humidity, weather.data.humidity)
        )
        TextMedium(
            text = context.getString(R.string.pressure, weather.data.pressure)
        )
        TextMedium(
            text = context.getString(R.string.wind, weather.data.windSpeed)
        )
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
