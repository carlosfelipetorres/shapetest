package dk.shape.forecast.usecases.places.placesdetail

import androidx.appcompat.app.AppCompatActivity
import dk.shape.forecast.usecases.places.placeslist.PlacesPresenter
import dk.shape.forecast.usecases.places.placeslist.PlacesPresenterImpl
import dk.shape.forecast.usecases.places.repository.PlacesRepository

/**
 * Configures the Places Use Case.
 *
 * @param activity The parent activity used to launch new activities and manage lifecycle events.
 * @param placesRepository Repository used to fetch Places.
 */
class PlaceDetailConfig(activity: AppCompatActivity,
                        placesRepository: PlacesRepository) {

    /**
     * Handles business logic to manipulate model objects and carry out tasks for
     * the Places use case.
     */
    val interactor: PlaceDetailInteractor = run {
        val interactor = PlaceDetailInteractorImpl(
                repository = placesRepository)
        activity.lifecycle.addObserver(interactor)
        interactor
    }

    /**
     * Converts data states from the Interactor into view states that are ready to be presented on
     * screen.
     */
    val presenter: PlaceDetailPresenter = PlaceDetailPresenterImpl(
            interactor = interactor
    )
}