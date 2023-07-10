package dev.kirstenbaker.gallery.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import dev.kirstenbaker.gallery.data.remote.GalleryDataApi
import dev.kirstenbaker.gallery.model.Artwork
import kotlinx.coroutines.flow.Flow
import java.net.UnknownHostException
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

    override suspend fun getArtworkById(id: Int): Artwork? {
        try {
            val artworkResponse = galleryDataApi.getArtworkById(id)
            if (artworkResponse.isSuccessful) {
                Log.d(
                    this.javaClass.name,
                    "GET request for artwork id $id was successful, code: " +
                            "${artworkResponse.code()}"
                )
                return artworkResponse.body()?.artwork
            } else {
                val errorString =
                    "HTTP response status code: ${artworkResponse.code()}, " +
                            "body: ${artworkResponse.errorBody()}"
                Log.d(
                    this.javaClass.name,
                    "GET request for artwork id $id failed with error: $errorString"
                )
                // In the case of an unsuccessful query, emit an empty list
                return null
            }
        } catch (e: Exception) {
            // basic error handling for a few potential exceptions: the JSON is typed incorrectly,
            // the JSON is malformed, or there's an issue with either the target host or the
            // device's internet connectivity more broadly
            if (e is JsonDataException || e is JsonEncodingException || e is UnknownHostException) {
                val errorString =
                    "exception caught while executing artwork id $id request: exception type " +
                            "${e.javaClass}, message: ${e.message}"
                Log.d(this.javaClass.name, errorString)
                // Emit an empty list rather than continuing display the previous output. This and
                // the similar emission above will make sense to change when we add offline
                // functionality
                return null
            } else {
                throw e
            }
        }
    }

    companion object {
        // from API docs
        const val apiPageSizeLimit = 10
    }
}