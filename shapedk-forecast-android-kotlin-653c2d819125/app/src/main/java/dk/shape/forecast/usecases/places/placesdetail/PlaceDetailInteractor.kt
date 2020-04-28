package dk.shape.forecast.usecases.places.placesdetail

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dk.shape.forecast.entities.Place
import dk.shape.forecast.usecases.places.repository.PlacesRepository

interface PlaceDetailInteractor {
    var onStateChanged: (State) -> Unit

    sealed class State {
        class Content(val place: Place) : State()
        object Loading : State()
        class Error(val onRetry: () -> Unit) : State()
    }
}

class PlaceDetailInteractorImpl(private val repository: PlacesRepository) : PlaceDetailInteractor, LifecycleObserver {

    override var onStateChanged: (PlaceDetailInteractor.State) -> Unit = {}
        set(value) {
            field = value
            onStateChanged(state)
        }

    private var state: PlaceDetailInteractor.State = PlaceDetailInteractor.State.Loading
        set(value) {
            if (field != value) {
                field = value
                onStateChanged(value)
            }
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        loadDetailData()
    }

    private fun loadDetailData() {
        state = PlaceDetailInteractor.State.Loading
        repository.placeDetail
                .onSuccess { place ->
                    state = PlaceDetailInteractor.State.Content(place = place)
                }
                .onError {
                    state = PlaceDetailInteractor.State.Error(
                            onRetry = {
                                loadDetailData()
                            }
                    )
                }
    }
}