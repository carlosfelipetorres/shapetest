package dk.shape.forecast

import androidx.appcompat.app.AppCompatActivity
import dk.shape.forecast.api.initHttpClient
import dk.shape.forecast.api.initWeatherAPI
import dk.shape.forecast.usecases.places.placesdetail.PlaceDetailConfig
import dk.shape.forecast.usecases.places.placeslist.PlacesConfig
import dk.shape.forecast.usecases.places.repository.PlacesRepository
import dk.shape.forecast.usecases.places.ui.PlaceDetailActivity

object AppConfig {
    private val woeIds = listOf(
            "2643743",
            "2950159",
            "3128760",
            "2267057",
            "2964574",
            "2618425",
            "524901",
            "5128581",
            "5375480",
            "2147714",
            "292223",
            "2988507")

    private val apiKey = "cf80aca9383a383c44505d1d09058553"

    private val httpClient by lazy { initHttpClient(apiKey) }
    private val weatherAPI by lazy { initWeatherAPI(httpClient) }

    /**
     * Initializes the Places use case configuration.
     *
     * @param The parent activity used to launch new activities and manage lifecycle events.
     */
    fun initPlacesConfig(activity: AppCompatActivity) =
            PlacesConfig(
                    activity = activity,
                    placesRepository = PlacesRepository(
                            weatherAPI = weatherAPI,
                            woeIds = woeIds))

    fun initPlaceDetailConfig(activity: AppCompatActivity) =
            PlaceDetailConfig(
                    activity = activity,
                    placesRepository = PlacesRepository(
                            weatherAPI = weatherAPI,
                            id = activity.intent.getStringExtra(PlaceDetailActivity.KEY_EXTRA_SELECTED_ID)))
}