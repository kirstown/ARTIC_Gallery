package dev.kirstenbaker.gallery.data.remote

import dev.kirstenbaker.gallery.data.dummy.artworkList
import dev.kirstenbaker.gallery.data.dummy.testArtwork1
import dev.kirstenbaker.gallery.model.ArtworkListResponse
import dev.kirstenbaker.gallery.model.ArtworkResponse
import dev.kirstenbaker.gallery.model.PaginationInfo
import retrofit2.Response

class FakeGalleryDataApi : GalleryDataApi {
    override suspend fun search(
        fields: String,
        query: String,
        pageIndex: Int,
        pageSizeLimit: Int
    ): Response<ArtworkListResponse> {
        return Response.success(ArtworkListResponse(artworkList = artworkList, paginationInfo = PaginationInfo(
            total = 200,
            totalPages = 20,
            currentPage = 1,
            limit = 10,
            offset = 0
        ), nextPage = null
        ))
    }

    override suspend fun getArtworkById(id: Int): Response<ArtworkResponse> {
        return Response.success(ArtworkResponse(testArtwork1))
    }
}