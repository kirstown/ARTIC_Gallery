package dev.kirstenbaker.gallery.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.kirstenbaker.gallery.data.remote.GalleryDataApi
import dev.kirstenbaker.gallery.model.Artwork
import retrofit2.HttpException
import java.io.IOException

private const val articStartingPageIndex = 1
private const val articPageSize = 10

/**
 * [PagingSource] that provides chunks of [Artwork] data indexed by [Int].
 *
 * See docs for info about how the API supports pagination: https://api.artic.edu/docs/#quick-start
 */
class GalleryPagingSource(
    private val galleryDataApi: GalleryDataApi,
    private val query: String
) : PagingSource<Int, Artwork>() {
    override fun getRefreshKey(state: PagingState<Int, Artwork>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artwork> {
        return try {
            val pageIndex = params.key ?: articStartingPageIndex
            val response = galleryDataApi.search(
                query = query,
                pageIndex = pageIndex,
                pageSizeLimit = articPageSize
            )

            if (response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()?.artworkList ?: listOf(),
                    prevKey = if (pageIndex == articStartingPageIndex) null else pageIndex.minus(1),
                    nextKey = if (response.body()?.artworkList?.isEmpty() == true) null else pageIndex.plus(
                        1
                    ),
                )
            } else {
                throw HttpException(response)
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
