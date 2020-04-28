package dk.shape.forecast.usecases.places.placeslist

import android.app.Activity
import android.content.Intent
import android.util.Log
import dk.shape.forecast.R
import dk.shape.forecast.usecases.places.ui.PlaceDetailActivity
import java.lang.ref.WeakReference

interface PlacesRouter {
    fun onPlaceSelected(woeid: String)
}

class PlacesRouterImpl(activity: Activity): PlacesRouter {
    override fun onPlaceSelected(woeid: String) {
        val intent = Intent(activityRef.get(), PlaceDetailActivity::class.java)
        intent.putExtra(PlaceDetailActivity.KEY_EXTRA_SELECTED_ID, woeid)
        activityRef.get()?.startActivity(intent)
    }

    private val activityRef = WeakReference<Activity>(activity)
}