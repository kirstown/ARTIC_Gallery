package dev.kirstenbaker.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import dev.kirstenbaker.gallery.BaseCoroutineTest
import dev.kirstenbaker.gallery.data.FakeGalleryDataRepositoryImpl
import dev.kirstenbaker.gallery.data.GalleryDataRepository
import dev.kirstenbaker.gallery.data.dummy.testArtwork1
import dev.kirstenbaker.gallery.data.remote.FakeGalleryDataApi
import dev.kirstenbaker.gallery.data.remote.GalleryDataApi
import dev.kirstenbaker.gallery.model.Artwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GalleryViewModelTest : BaseCoroutineTest() {
    private lateinit var galleryDataApi: GalleryDataApi
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var galleryDataRepository: GalleryDataRepository

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle().apply {
            set(lastSearchQuery, null)
        }
        galleryDataApi = FakeGalleryDataApi()
        galleryDataRepository = FakeGalleryDataRepositoryImpl()
        galleryViewModel = GalleryViewModel(
            savedState = savedStateHandle,
            galleryDataRepository = galleryDataRepository
        )
    }

    @Test
    fun `GalleryViewModel initial state holds empty query`() = runTest {
        val actual = galleryViewModel.state.value.query
        val expected = ""

        assertEquals(expected, actual)
    }

    @Test
    fun `GalleryViewModel transforms search action into correct state`() = runTest {

        galleryViewModel.ingestAction(GalleryListUiAction.Search(query = "test"))

        val actual = galleryViewModel.state.value.query
        val expected = "test"

        assertEquals(expected, actual)
    }

    @Test
    fun `GalleryViewModel transforms holds search query based on most recent action`() = runTest {

        galleryViewModel.ingestAction(GalleryListUiAction.Search(query = "test1"))
        galleryViewModel.ingestAction(GalleryListUiAction.Search(query = "test2"))

        val actual = galleryViewModel.state.value.query
        val expected = "test2"

        assertEquals(expected, actual)
    }

    @Test
    fun scrollingShowsNewItems() = runTest {
        val items: Flow<PagingData<Artwork>> = galleryViewModel.pagingDataFlow

        val itemsSnapshot: List<Artwork> = items.asSnapshot {
            scrollTo(index = 0)
        }

        val expectedList = (0 until 30).map { num ->
            // this behavior matches the test data; indices should be increasing
            testArtwork1.copy(id = num)
        }

        assertEquals(
            expectedList,
            itemsSnapshot
        )
    }

    @Test
    fun scrollingTo0Generates30Items() = runTest {
        val items: Flow<PagingData<Artwork>> = galleryViewModel.pagingDataFlow

        val itemsSnapshot: List<Artwork> = items.asSnapshot {
            scrollTo(index = 0)
        }

        val expectedList = (0 until 30).map { num ->
            testArtwork1
        }
        assertEquals(expectedList.size, itemsSnapshot.size)
    }

    @Test
    fun scrollingTo10Generates30Items() = runTest {
        val items: Flow<PagingData<Artwork>> = galleryViewModel.pagingDataFlow

        val itemsSnapshot: List<Artwork> = items.asSnapshot {
            scrollTo(index = 10)
        }
        val expectedList = (0 until 30).map { num ->
            testArtwork1
        }
        assertEquals(expectedList.size, itemsSnapshot.size)
    }

    @Test
    fun scrollingTo50Generates70Items() = runTest {
        val items: Flow<PagingData<Artwork>> = galleryViewModel.pagingDataFlow

        val itemsSnapshot: List<Artwork> = items.asSnapshot {
            scrollTo(index = 50)
        }

        val expectedList = (0 until 70).map { num ->
            testArtwork1
        }
        assertEquals(expectedList.size, itemsSnapshot.size)
    }

    @Test
    fun scrollingTo80Generates100Items() = runTest {
        val items: Flow<PagingData<Artwork>> = galleryViewModel.pagingDataFlow

        val itemsSnapshot: List<Artwork> = items.asSnapshot {
            scrollTo(index = 80)
        }

        val expectedList = (0 until 100).map { num ->
            testArtwork1
        }
        assertEquals(expectedList.size, itemsSnapshot.size)
    }

    @Test
    fun scrollingTo150Generates170Items() = runTest {
        val items: Flow<PagingData<Artwork>> = galleryViewModel.pagingDataFlow

        val itemsSnapshot: List<Artwork> = items.asSnapshot {
            scrollTo(index = 150)
        }

        val expectedList = (0 until 170).map { num ->
            testArtwork1
        }
        assertEquals(expectedList.size, itemsSnapshot.size)
    }
}