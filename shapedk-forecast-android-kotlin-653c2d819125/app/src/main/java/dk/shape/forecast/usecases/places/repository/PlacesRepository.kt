package dk.shape.forecast.usecases.places.repository

import dk.shape.forecast.api.WeatherAPI
import dk.shape.forecast.entities.Place
import dk.shape.forecast.framework.Promise
import dk.shape.forecast.entities.Temperature
import dk.shape.forecast.entities.TemperatureUnit
import dk.shape.forecast.usecases.places.repository.mapping.SimplePlace
import dk.shape.forecast.usecases.places.repository.mapping.SimplePlaceGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class PlacesRepository(
        private val weatherAPI: WeatherAPI,
        woeIds: List<String>? = null,
        private val id: String? = null
) {

    private val ids = woeIds?.joinToString(separator = ",") ?: ""

    /**
     * Returns a Promise that will at some point return the List of Places.
     */
    val places: Promise<List<Place>>
        get() {
            val promise = Promise<List<Place>>()

            weatherAPI.placesQuery(ids = ids)
                    .enqueue(object : Callback<SimplePlaceGroup> {
                        override fun onResponse(call: Call<SimplePlaceGroup>, response: Response<SimplePlaceGroup>) {
                            if (!response.isSuccessful) {
                                promise.error(IOException("Response was unsuccessful: ${response.code()}"))
                                return
                            }

                            val result = response.body()
                            if (result == null) {
                                promise.error(IOException("Response has no body"))
                                return
                            }

                            result.let { placeGroup ->
                                promise.success(placeGroup.asPlaces())
                            }
                        }

                        override fun onFailure(call: Call<SimplePlaceGroup>, t: Throwable) {
                            promise.error(t)
                        }
                    })

            return promise
        }

    /**
     * Returns a Promise that will at some point return the specific Place.
     */
    val placeDetail: Promise<Place>
        get() {
            val promise = Promise<Place>()

            id?.let {
                weatherAPI.placeDetailQuery(it)
                        .enqueue(object : Callback<SimplePlace> {
                            override fun onResponse(call: Call<SimplePlace>, response: Response<SimplePlace>) {
                                if (!response.isSuccessful) {
                                    promise.error(IOException("Response was unsuccessful: ${response.code()}"))
                                    return
                                }

                                val result = response.body()
                                if (result == null) {
                                    promise.error(IOException("Response has no body"))
                                    return
                                }

                                result.let { place ->
                                    place.asPlace()?.let { promise.success(it) }
                                }
                            }

                            override fun onFailure(call: Call<SimplePlace>, t: Throwable) {
                                promise.error(t)
                            }
                        })
            }

            return promise
        }
}

private fun SimplePlaceGroup.asPlaces(): List<Place> {
    return places.orEmpty().mapNotNull { it.asPlace() }
}

private fun SimplePlace.asPlace(): Place? {
    val woeId = id?.toString() ?: return null
    val weatherCode = weathers?.firstOrNull()?.id ?: return null
    val weatherDescription = weathers.firstOrNull()?.description ?: return null
    val weatherIcon = weathers.firstOrNull()?.icon ?: return null
    val city = name ?: return null
    val code = details?.countryCode
    val country = getCountryNameFromCountryCode(code) ?: return null
    val temperature = parameters?.temperature?.toInt() ?: return null
    val sunrise = details?.sunrise
    val sunset = details?.sunset
    val speedWind = wind?.speed

    return Place(
            woeId = woeId,
            city = city,
            country = country,
            code = code,
            temperature = Temperature(
                    value = temperature,
                    unit = TemperatureUnit.Celsius),
            weatherCode = weatherCode,
            weatherDescription = weatherDescription,
            weatherIcon = weatherIcon,
            sunrise = sunrise,
            sunset = sunset,
            speedWind = speedWind)
}

private fun getCountryNameFromCountryCode(countryCode: String?): String? {
    val code = countryCode ?: return null
    return Locale("", code).displayCountry
}