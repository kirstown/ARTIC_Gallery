package dev.kirstenbaker.gallery.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Server response class for [Artwork].
 */
@JsonClass(generateAdapter = true)
data class ArtworkResponse(
    @Json(name = "data")
    val artwork: Artwork,
)