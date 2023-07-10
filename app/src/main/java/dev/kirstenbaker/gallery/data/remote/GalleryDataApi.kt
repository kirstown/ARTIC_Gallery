package dev.kirstenbaker.gallery.data.remote

import dev.kirstenbaker.gallery.model.ArtworkListResponse
import dev.kirstenbaker.gallery.model.ArtworkResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val defaultFields =
    "id,title,artist_display,date_display,main_reference_number,image_id,medium_display,place_of_origin,is_on_view,gallery_title,artist_title"

/**
    See ARTIC API docs here: https://api.artic.edu/docs/#quick-start
 */
interface GalleryDataApi {
    @GET("api/v1/artworks/search")
    suspend fun search(
        @Query("fields") fields: String = defaultFields,
        @Query("q") query: String,
        @Query("page") pageIndex: Int,
        @Query("limit") pageSizeLimit: Int
    ): Response<ArtworkListResponse>

    @GET("api/v1/artworks/{id}")
    suspend fun getArtworkById(@Path("id") id: Int): Response<ArtworkResponse>
}