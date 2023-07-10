package dev.kirstenbaker.gallery.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kirstenbaker.gallery.data.GalleryDataRepository
import dev.kirstenbaker.gallery.idNavInt
import dev.kirstenbaker.gallery.model.Artwork
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Artwork detail implementation of [ViewModel]. Unlike [GalleryViewModel], this VM does not
 * formalize the transformation from UI actions into UI state since there are no user input events
 * on this screen. I was on the fence about storing this state as individual fields vs. a single
 * data class, but I think if I were to improve this app, I would refactor this to use a unified
 * state entity.
 */
@HiltViewModel
class ArtworkDetailViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val galleryDataRepository: GalleryDataRepository,
) : ViewModel(), LifecycleObserver {

    private val _artworkState: MutableState<Artwork?> =
        mutableStateOf(null)
    val artworkState: State<Artwork?> get() = _artworkState

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    init {
        // inspiration from here: https://github.com/fvilarino/Navigation-Sample
        val navArgId = savedState.get<Int>(idNavInt)
        navArgId?.let { id ->
            _isLoading.value = true
            loadArtwork(id)
        }
    }

    private fun loadArtwork(id: Int) {
        viewModelScope.launch {
            _artworkState.value = galleryDataRepository.getArtworkById(id)
            _isLoading.value = false
        }
    }
}
