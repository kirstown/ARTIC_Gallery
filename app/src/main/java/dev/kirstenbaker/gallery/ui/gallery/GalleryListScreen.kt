package dev.kirstenbaker.gallery.ui.gallery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import dev.kirstenbaker.gallery.R
import dev.kirstenbaker.gallery.model.Artwork
import dev.kirstenbaker.gallery.ui.ProgressIndicator
import dev.kirstenbaker.gallery.ui.theme.GalleryTheme
import dev.kirstenbaker.gallery.viewmodel.GalleryListUiAction
import dev.kirstenbaker.gallery.viewmodel.GalleryListUiState
import dev.kirstenbaker.gallery.viewmodel.GalleryViewModel

private val minCardSize = 320.dp
private val columnHorizontalPaddingSize = 80.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryListScreen(
    modifier: Modifier = Modifier,
    galleryViewModel: GalleryViewModel,
    onClickArtwork: (Int) -> Unit,
) {
    val searchResultState = galleryViewModel.pagingDataFlow.collectAsLazyPagingItems()
    val viewModelState = galleryViewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            ArtworkListTopBar(
                scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { scaffoldPaddingValues ->
        GalleryList(
            modifier = Modifier.padding(scaffoldPaddingValues),
            searchResultState = searchResultState,
            loadState = searchResultState.loadState,
            itemCount = searchResultState.itemCount,
            viewModelState = viewModelState,
            ingestAction = galleryViewModel.ingestAction,
            onClickArtwork = onClickArtwork
        )
    }
}

@Composable
fun GalleryList(
    modifier: Modifier = Modifier,
    searchResultState: LazyPagingItems<Artwork>,
    loadState: CombinedLoadStates,
    itemCount: Int,
    viewModelState: State<GalleryListUiState>,
    ingestAction: (GalleryListUiAction) -> Unit,
    onClickArtwork: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
    ) {
        SearchBar(
            queryString = viewModelState.value.query,
            onSearch = { query ->
                ingestAction(
                    GalleryListUiAction.Search(
                        query
                    )
                )
            })
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                // This branch is hit when a list is successfully loaded, but it's empty.
                searchResultState.isEmptyList -> {
                    GalleryListEmptyPlaceholder(
                        modifier = Modifier
                            .padding(horizontal = columnHorizontalPaddingSize)
                    )
                }

                // This branch is hit when the first page of the pagination request fails.
                loadState.refresh is LoadState.Error -> {
                    GalleryListEmptyPlaceholderWithError(
                        modifier = Modifier
                            .padding(horizontal = columnHorizontalPaddingSize),
                        onRetry = { searchResultState.retry() }
                    )
                }

                // This branch is hit when the first page of the pagination request is still
                // loading.
                loadState.refresh is LoadState.Loading -> {
                    ProgressIndicator(
                        modifier = Modifier
                            .padding(horizontal = columnHorizontalPaddingSize)
                    )
                }

                // Main list UI is all here: hitting this branch means that at least one page of a
                // non-empty list has been loaded.
                else -> {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.Center),
                        columns = GridCells.Adaptive(minSize = minCardSize),
                        state = rememberLazyGridState()
                    ) {
                        items(itemCount) { index ->
                            searchResultState[index]?.let { resultArtwork ->
                                ArtworkCard(
                                    artwork = resultArtwork,
                                    onClickArtwork = { onClickArtwork(resultArtwork.id) })
                            }
                        }

                        // Pagination footer UI
                        when (loadState.append) {
                            is LoadState.Error -> {
                                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                                    PaginationFailedFooter() {
                                        searchResultState.retry()
                                    }
                                }
                            }

                            is LoadState.Loading -> {
                                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                                    ProgressIndicator()
                                }
                            }

                            else -> {
                                /* No-op, we won't show anything when not loading in the append
                                case. The main item list takes care of displaying new items */
                            }
                        }
                    }
                }
            }
        }
    }
}

// Extension function to use when determining whether to show empty state, reference:
// https://stackoverflow.com/a/74699019
private val <T : Any> LazyPagingItems<T>.isEmptyList
    get() = loadState.append.endOfPaginationReached && itemCount == 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworkListTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
) {
    LargeTopAppBar(
        title = { Text(text = stringResource(R.string.gallery_list_header)) },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ArtworkListTopBarPreview() {
    GalleryTheme {
        ArtworkListTopBar(
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        )
    }
}