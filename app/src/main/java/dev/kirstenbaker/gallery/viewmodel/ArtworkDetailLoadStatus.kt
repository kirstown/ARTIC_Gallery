package dev.kirstenbaker.gallery.viewmodel

import dev.kirstenbaker.gallery.model.Artwork

/**
 * Sealed class representing UI load status of artwork detail screen.
 */
sealed class ArtworkDetailLoadStatus {
    /**
     * Indicates a successful, non-null [artwork] load.
     */
    class Success(val artwork: Artwork): ArtworkDetailLoadStatus()

    /**
     * Indicates an unsuccessful load. [errorString] is not shown as part of the UI, but it's
     * included for debugging purposes.
     */
    class Failure(private val errorString: String): ArtworkDetailLoadStatus()

    /**
     * Indicates the load is in progress.
     */
    object Loading : ArtworkDetailLoadStatus()
}