package dev.kirstenbaker.gallery.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.testing.asPagingSourceFactory
import dev.kirstenbaker.gallery.data.dummy.artworkList
import dev.kirstenbaker.gallery.data.dummy.testArtwork1
import dev.kirstenbaker.gallery.model.Artwork
import kotlinx.coroutines.flow.Flow

class FakeGalleryDataRepositoryImpl :
    GalleryDataRepository {

    override fun getSearchResultStream(query: String): Flow<PagingData<Artwork>> {
        val pagingSourceFactory = artworkList.asPagingSourceFactory().invoke()

        return Pager(
            config = PagingConfig(
                pageSize = GalleryDataRepositoryImpl.apiPageSizeLimit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { pagingSourceFactory }
        ).flow
    }

    override suspend fun getArtworkById(id: Int): Artwork {
        return testArtwork1.copy(id = id)
    }
}