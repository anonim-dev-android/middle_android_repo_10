package ru.yandex.buggyweatherapp.data.model

data class WeatherData(
    val cityName: String,
    val country: String,
    val temperature: Double,
    val feelsLike: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val description: String,
    val icon: String,
    val rain: Double? = null,
    val snow: Double? = null,
    val cloudiness: Int,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val timezone: Int,
    val timestamp: Long,

    val rawApiData: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherData

        // comparing only unique weather data
        if (cityName != other.cityName) return false
        if (country != other.country) return false
        if (temperature != other.temperature) return false
        if (feelsLike != other.feelsLike) return false
        if (minTemp != other.minTemp) return false
        if (maxTemp != other.maxTemp) return false
        if (humidity != other.humidity) return false
        if (pressure != other.pressure) return false
        if (windSpeed != other.windSpeed) return false
        if (windDirection != other.windDirection) return false
        if (description != other.description) return false
        if (icon != other.icon) return false
        if (rain != other.rain) return false
        if (snow != other.snow) return false
        if (cloudiness != other.cloudiness) return false
        if (sunriseTime != other.sunriseTime) return false
        if (sunsetTime != other.sunsetTime) return false
        if (timezone != other.timezone) return false
        // except timestamp, rawApiData
        return true
    }

    override fun hashCode(): Int {
        var result = cityName.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + temperature.hashCode()
        result = 31 * result + feelsLike.hashCode()
        result = 31 * result + minTemp.hashCode()
        result = 31 * result + maxTemp.hashCode()
        result = 31 * result + humidity
        result = 31 * result + pressure
        result = 31 * result + windSpeed.hashCode()
        result = 31 * result + windDirection
        result = 31 * result + description.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + (rain?.hashCode() ?: 1)
        result = 31 * result + (snow?.hashCode() ?: 2)
        result = 31 * result + cloudiness
        result = 31 * result + sunriseTime.hashCode()
        result = 31 * result + sunsetTime.hashCode()
        result = 31 * result + timezone
        result = 31 * result + timestamp.hashCode()
        return result
    }
}