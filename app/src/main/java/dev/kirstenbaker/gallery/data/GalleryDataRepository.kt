package dev.kirstenbaker.gallery.data

import androidx.paging.PagingData
import dev.kirstenbaker.gallery.model.Artwork
import dev.kirstenbaker.gallery.model.RemoteResult
import kotlinx.coroutines.flow.Flow

/**
 * This interface lays out the primary methods for gallery data retrieval. It's important to note
 * that this app currently only pulls from the remote source, so there's no DB loading/querying.
 * This is an interface rather than a class in order to allow for a version used in unit tests.
 *
 * [getSearchResultStream] Stream that provides paginated [Artwork] data as they're requested by
 * the UI state (either scrolling or searching).
 * [getArtworkById] Async function that loads an [Artwork] [RemoteResult] instance based on the
 * piece's ID and the server response.
 */
interface GalleryDataRepository {
    fun getSearchResultStream(query: String): Flow<PagingData<Artwork>>

    suspend fun getArtworkById(id: Int): RemoteResult<Artwork>
}

