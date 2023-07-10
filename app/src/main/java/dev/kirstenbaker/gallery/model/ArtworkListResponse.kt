package dev.kirstenbaker.gallery.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Server response class for list of [Artwork] that also includes pagination data.
 */
@JsonClass(generateAdapter = true)
data class ArtworkListResponse(
    @Json(name = "data")
    val artworkList: List<Artwork>,
    @Json(name = "pagination")
    val paginationInfo: PaginationInfo,
    val nextPage: Int? = null
)

