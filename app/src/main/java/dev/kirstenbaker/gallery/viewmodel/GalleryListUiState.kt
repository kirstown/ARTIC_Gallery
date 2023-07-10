package dev.kirstenbaker.gallery.viewmodel

/**
 * Class representing the UI state of the gallery list.
 *
 * [query] The current search query.
 */
data class GalleryListUiState(
    val query: String = defaultQuery,
)