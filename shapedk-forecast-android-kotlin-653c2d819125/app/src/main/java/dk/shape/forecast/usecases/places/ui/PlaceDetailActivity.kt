package dk.shape.forecast.usecases.places.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import dk.shape.forecast.AppConfig
import dk.shape.forecast.R
import dk.shape.forecast.usecases.places.placesdetail.PlaceDetailPresenter
import dk.shape.forecast.utils.ui.colorResourceToStateList
import kotlinx.android.synthetic.main.activity_place_detail.*
import java.text.DateFormat
import java.util.*


class PlaceDetailActivity : AppCompatActivity() {

    companion object {
        val KEY_EXTRA_SELECTED_ID = "selectedId"
    }

    private val placeDetailConfig by lazy { AppConfig.initPlaceDetailConfig(activity = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        placeDetailConfig.presenter.onStateChanged = { state ->
            when (state) {
                is PlaceDetailPresenter.State.Content -> {
                    val config = state.uiConfigs
                    textTemperature.text = config.temperature
                    backgroundTemperature.imageTintList = config.temperatureColorResource.colorResourceToStateList(context = this)
                    textTemperature.text = config.temperature
                    textCity.text = config.city
                    textCountry.text = config.country
                    windTitle.text = "${config.wind} m/sec"
                    weatherDescription.text = config.weatherDescription

                    val sunriseDate = config.sunriseTime?.let { Date(it) }
                    sunriseTime.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(sunriseDate)
                    val sunsetDate = config.sunsetTime?.let { Date(it) }
                    sunsetTime.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(sunsetDate)

                    Glide.with(this).load(config.weather).into(weatherIcon)
                    Glide.with(this).load(config.flag).into(flagIcon)
                    weatherIcon.imageTintList = config.temperatureColorResource.colorResourceToStateList(context = this)
                    windIcon.imageTintList = config.temperatureColorResource.colorResourceToStateList(context = this)

                    loadingView.visibility = View.GONE
                    errorView.visibility = View.GONE

                    temperature.visibility = View.VISIBLE
                    location.visibility = View.VISIBLE
                    windCard.visibility = View.VISIBLE
                    descriptionCard.visibility = View.VISIBLE
                    sunriseCard.visibility = View.VISIBLE
                    sunsetCard.visibility = View.VISIBLE
                }
                is PlaceDetailPresenter.State.Loading -> {
                    loadingView.visibility = View.VISIBLE
                    errorView.visibility = View.GONE

                    temperature.visibility = View.GONE
                    location.visibility = View.GONE
                    windCard.visibility = View.GONE
                    descriptionCard.visibility = View.GONE
                    sunriseCard.visibility = View.GONE
                    sunsetCard.visibility = View.GONE
                }
                is PlaceDetailPresenter.State.Error -> {
                    loadingView.visibility = View.GONE
                    errorView.visibility = View.VISIBLE

                    temperature.visibility = View.GONE
                    location.visibility = View.GONE
                    windCard.visibility = View.GONE
                    descriptionCard.visibility = View.GONE
                    sunriseCard.visibility = View.GONE
                    sunsetCard.visibility = View.GONE

                    errorRetryButton.setOnClickListener {
                        state.onRetry()
                    }
                }
            }
        }
    }
}
