package dev.kirstenbaker.gallery.data

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import dev.kirstenbaker.gallery.data.dummy.artworkList
import dev.kirstenbaker.gallery.data.remote.FakeGalleryDataApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GalleryDataRepositoryTest {
    private val defaultQuery = "test"
    private val fakeApi = FakeGalleryDataApi()

    @Test
    fun `GalleryPagingSource returns successful page on valid refresh call`() = runTest {
        val pagingSource = GalleryPagingSource(
            fakeApi,
            defaultQuery
        )

        val pager = TestPager(
            PagingConfig(
                pageSize = GalleryDataRepositoryImpl.apiPageSizeLimit,
                enablePlaceholders = false
            ), pagingSource
        )

        val result = pager.refresh() as PagingSource.LoadResult.Page

        assertEquals(result.data.size, artworkList.size)
    }
}