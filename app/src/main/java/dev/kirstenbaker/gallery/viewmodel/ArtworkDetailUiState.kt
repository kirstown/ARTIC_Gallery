package dev.kirstenbaker.gallery.viewmodel

/**
 * Class representing the UI state of the artwork detail screen.
 *
 * [loadStatus] The current load state of the artwork.
 */
data class ArtworkDetailUiState(
    val loadStatus: ArtworkDetailLoadStatus
)
