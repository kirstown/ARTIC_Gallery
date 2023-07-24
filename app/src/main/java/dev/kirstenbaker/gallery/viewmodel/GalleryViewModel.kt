package dev.kirstenbaker.gallery.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kirstenbaker.gallery.data.GalleryDataRepository
import dev.kirstenbaker.gallery.model.Artwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val lastSearchQuery: String = "last_search_query"
const val defaultQuery = ""

/**
 * Gallery list implementation of [ViewModel] that receives [GalleryListUiAction] events via
 * [ingestAction] and transforms them into correct pagination behavior emitted as
 * [GalleryListUiAction]. [defaultQuery] starts as empty and returns all data.
 *
 * This implementation is inspired by the [ViewModel] implementation in the Advanced
 * Paging Codelab: https://developer.android.com/codelabs/android-paging. I liked the specific use
 * of UI action -> state handling as well as the nice flow manipulations.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    val galleryDataRepository: GalleryDataRepository
) : ViewModel(), LifecycleObserver {

    val state: StateFlow<GalleryListUiState>
    val pagingDataFlow: Flow<PagingData<Artwork>>
    val ingestAction: (GalleryListUiAction) -> Unit

    init {
        val initialQuery: String = savedState[lastSearchQuery] ?: defaultQuery
        val actionStateFlow = MutableSharedFlow<GalleryListUiAction>()
        val searches = actionStateFlow
            .filterIsInstance<GalleryListUiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(GalleryListUiAction.Search(query = initialQuery)) }

        pagingDataFlow = searches
            .flatMapLatest {
                searchArtwork(queryString = it.query)
            }
            .cachedIn(viewModelScope)

        state =
            searches
                .map { search ->
                    GalleryListUiState(
                        query = search.query,
                    )
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = GalleryListUiState()
                )

        ingestAction = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }

    override fun onCleared() {
        savedState[lastSearchQuery] = state.value.query
        super.onCleared()
    }

    private fun searchArtwork(queryString: String): Flow<PagingData<Artwork>> {
        return galleryDataRepository.getSearchResultStream(queryString)
    }
}
