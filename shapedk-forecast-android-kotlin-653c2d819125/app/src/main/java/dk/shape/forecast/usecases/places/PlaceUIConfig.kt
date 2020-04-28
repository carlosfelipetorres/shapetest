package dk.shape.forecast.usecases.places

class PlaceUIConfig(val city: String,
                    val country: String,
                    val temperature: String,
                    val temperatureColorResource: Int,
                    val onClick: () -> Unit,
                    val flag: String? = null,
                    val wind: Double? = null,
                    val weather: String? = null,
                    val weatherDescription: String? = null,
                    val sunriseTime: Long? = null,
                    val sunsetTime: Long? = null)