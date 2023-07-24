package dev.kirstenbaker.gallery.viewmodel

import androidx.lifecycle.SavedStateHandle
import dev.kirstenbaker.gallery.BaseCoroutineTest
import dev.kirstenbaker.gallery.data.FakeGalleryDataRepositoryImpl
import dev.kirstenbaker.gallery.data.GalleryDataRepository
import dev.kirstenbaker.gallery.data.dummy.artworkList
import dev.kirstenbaker.gallery.data.remote.FakeGalleryDataApi
import dev.kirstenbaker.gallery.data.remote.GalleryDataApi
import dev.kirstenbaker.gallery.idNavInt
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ArtworkDetailViewModelTest : BaseCoroutineTest() {
    private lateinit var galleryDataApi: GalleryDataApi
    private lateinit var artworkDetailViewModel: ArtworkDetailViewModel
    private lateinit var galleryDataRepository: GalleryDataRepository

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle().apply {
            set(idNavInt, 0)
        }
        galleryDataApi = FakeGalleryDataApi()
        galleryDataRepository = FakeGalleryDataRepositoryImpl()
        artworkDetailViewModel = ArtworkDetailViewModel(
            savedState = savedStateHandle,
            galleryDataRepository = galleryDataRepository
        )
    }

    @Test
    fun `ArtworkDetailViewModel initializes valid artwork based on nav args`() = runTest {
        val actual =
            (artworkDetailViewModel.state.value.loadStatus as ArtworkDetailLoadStatus.Success).artwork
        val expected = artworkList.firstOrNull { artwork -> artwork.id == 0 }

        Assert.assertEquals(expected, actual)
    }
}