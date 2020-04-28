package dk.shape.forecast.usecases.places.repository.mapping

import com.google.gson.annotations.SerializedName

data class SimplePlaceWind(
        @SerializedName("speed") val speed: Double?,
        @SerializedName("deg") val deg: Double?
)