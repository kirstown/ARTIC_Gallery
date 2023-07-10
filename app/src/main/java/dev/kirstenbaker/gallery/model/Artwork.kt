package dev.kirstenbaker.gallery.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

/**
 * Class representing local version of [Artwork] object from server using fields queried.
 */
@JsonClass(generateAdapter = true)
data class Artwork(
    val id: Int,
    val title: String?,
    @Json(name = "artist_title")
    val artistTitle: String?, // Artist name or cultural source only
    @Json(name = "artist_display")
    val artistDisplay: String?, // Includes additional artist info
    @Json(name = "date_display")
    val dateDisplay: String?,
    @Json(name = "image_id")
    val imageId: String?, // Type: UUID
    @Json(name = "medium_display")
    val mediumDisplay: String?,
    @Json(name = "place_of_origin")
    val placeOfOrigin: String?,
    @Json(name = "is_on_view")
    val isOnView: Boolean?,
    @Json(name = "gallery_title")
    val galleryTitle: String?,
) : Serializable