package dev.kirstenbaker.gallery.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.kirstenbaker.gallery.data.remote.GalleryDataApi
import dev.kirstenbaker.gallery.model.Artwork
import dev.kirstenbaker.gallery.model.RemoteResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryDataRepositoryImpl @Inject constructor(private val galleryDataApi: GalleryDataApi) :
    GalleryDataRepository {

    override fun getSearchResultStream(query: String): Flow<PagingData<Artwork>> {
        val pagingSource = GalleryPagingSource(galleryDataApi, query)
        return Pager(
            config = PagingConfig(
                pageSize = apiPageSizeLimit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { pagingSource }
        ).flow
    }

    override suspend fun getArtworkById(id: Int): RemoteResult<Artwork> {
        try {
            val artworkResponse = galleryDataApi.getArtworkById(id)
            if (artworkResponse.isSuccessful) {
                val responseBody = artworkResponse.body()
                return if (responseBody != null) {
                    Log.d(
                        this.javaClass.name,
                        "GET request for artwork id $id was successful, code: " +
                                "${artworkResponse.code()}"
                    )
                    RemoteResult.Success(responseBody.artwork)
                } else {
                    val errorString =
                        "GET request returned with null response body for artwork id $id, code: ${artworkResponse.code()}"
                    Log.d(this.javaClass.name, errorString)
                    RemoteResult.Failure(errorString)
                }
            } else {
                val errorString =
                    "GET request failed for artwork id $id with HTTP response status code: ${artworkResponse.code()}, " +
                            "body: ${artworkResponse.errorBody()}"
                Log.d(this.javaClass.name, errorString)
                return RemoteResult.Failure(errorString)
            }
        } catch (e: Exception) {
            // Catch all exceptions here and return failure result type with error string included
            // for context
            val errorString =
                "Exception caught while executing artwork id $id request: exception type " +
                        "${e.javaClass}, message: ${e.message}"
            Log.d(this.javaClass.name, errorString)
            return RemoteResult.Failure(errorString)
        }
    }

    companion object {
        // from API docs
        const val apiPageSizeLimit = 10
    }
}