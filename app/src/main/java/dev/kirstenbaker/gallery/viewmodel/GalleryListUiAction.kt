package dev.kirstenbaker.gallery.viewmodel

/**
 * Class representing user actions possible on the gallery list screen.
 *
 * [Search] The user submits a search query via the search bar.
 */
sealed class GalleryListUiAction {
    data class Search(val query: String) : GalleryListUiAction()
}