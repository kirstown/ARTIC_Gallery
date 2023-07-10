package dev.kirstenbaker.gallery.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class representing pagination information for list of [Artwork].
 */
@JsonClass(generateAdapter = true)
data class PaginationInfo(
    val total: Int,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "current_page")
    val currentPage: Int,
    val limit: Int,
    val offset: Int
)