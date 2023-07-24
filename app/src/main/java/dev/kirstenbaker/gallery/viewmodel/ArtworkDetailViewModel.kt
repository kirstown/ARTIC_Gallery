package dev.kirstenbaker.gallery.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kirstenbaker.gallery.data.GalleryDataRepository
import dev.kirstenbaker.gallery.idNavInt
import dev.kirstenbaker.gallery.model.Artwork
import dev.kirstenbaker.gallery.model.RemoteResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Artwork detail implementation of [ViewModel]. Unlike [GalleryViewModel], this VM does not
 * formalize the transformation from UI actions into UI state since there are no user input events
 * on this screen. The artwork's ID is stored and restored as a nav argument, and the UI state is
 * represented by the [ArtworkDetailUiState] class.
 */
@HiltViewModel
class ArtworkDetailViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val galleryDataRepository: GalleryDataRepository,
) : ViewModel(), LifecycleObserver {

    // start VM in loading state
    private val _state: MutableStateFlow<ArtworkDetailUiState> =
        MutableStateFlow(ArtworkDetailUiState(loadStatus = ArtworkDetailLoadStatus.Loading))
    val state: StateFlow<ArtworkDetailUiState> get() = _state.asStateFlow()

    init {
        // inspiration from here: https://github.com/fvilarino/Navigation-Sample
        val navArgId = savedState.get<Int>(idNavInt)
        if (navArgId != null) {
            loadArtwork(navArgId)
        } else {
            _state.value =
                ArtworkDetailUiState(loadStatus = ArtworkDetailLoadStatus.Failure("Failed to extract artwork ID from navigation arguments"))
        }
    }

    private fun loadArtwork(id: Int) {
        viewModelScope.launch {
            val artworkResult: RemoteResult<Artwork> = galleryDataRepository.getArtworkById(id)
            _state.value = when (artworkResult) {
                is RemoteResult.Success -> ArtworkDetailUiState(
                    loadStatus = ArtworkDetailLoadStatus.Success(
                        artworkResult.data
                    )
                )

                is RemoteResult.Failure -> {
                    Log.d(
                        this.javaClass.name,
                        "Artwork load failed with the following error: ${artworkResult.errorMessage}"
                    )
                    ArtworkDetailUiState(
                        loadStatus = ArtworkDetailLoadStatus.Failure(
                            artworkResult.errorMessage
                        )
                    )
                }
            }
        }
    }
}
