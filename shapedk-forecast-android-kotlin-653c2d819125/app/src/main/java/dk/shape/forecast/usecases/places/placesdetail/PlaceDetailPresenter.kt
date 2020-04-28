package dk.shape.forecast.usecases.places.placesdetail

import dk.shape.forecast.usecases.places.PlaceUIConfig
import dk.shape.forecast.utils.ui.toColorResource
import java.text.DateFormat
import java.util.*

interface PlaceDetailPresenter {
    var onStateChanged: (State) -> Unit

    sealed class State {
        class Content(val uiConfigs: PlaceUIConfig) : State()
        object Loading : State()
        class Error(val onRetry: () -> Unit) : State()
    }
}

class PlaceDetailPresenterImpl(interactor: PlaceDetailInteractor) : PlaceDetailPresenter {

    override var onStateChanged: (PlaceDetailPresenter.State) -> Unit = {}
        set(value) {
            field = value
            onStateChanged(state)
        }

    private var state: PlaceDetailPresenter.State = PlaceDetailPresenter.State.Loading
        set(value) {
            if (field != value) {
                field = value
                onStateChanged(value)
            }
        }

    init {
        interactor.onStateChanged = { state ->
            this.state = when (state) {
                is PlaceDetailInteractor.State.Content -> state.map()
                is PlaceDetailInteractor.State.Loading -> PlaceDetailPresenter.State.Loading
                is PlaceDetailInteractor.State.Error -> PlaceDetailPresenter.State.Error(
                        onRetry = { state.onRetry() }
                )
            }
        }
    }
}

private fun PlaceDetailInteractor.State.Content.map(): PlaceDetailPresenter.State.Content {
    val uiConfigs = place.let {

        PlaceUIConfig(
                city = it.city,
                country = it.country,
                flag = "https://www.countryflags.io/${it.code}/flat/64.png",
                temperature = "${it.temperature.value} ${it.temperature.unit.postFix}",
                temperatureColorResource = it.temperature.toColorResource(),
                wind = it.speedWind,
                weather = "https://openweathermap.org/img/wn/${it.weatherIcon}@2x.png",
                weatherDescription = it.weatherDescription,
                sunriseTime = it.sunrise,
                sunsetTime = it.sunset,
                onClick = {})
    }
    return PlaceDetailPresenter.State.Content(uiConfigs = uiConfigs)
}