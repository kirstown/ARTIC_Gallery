package dev.kirstenbaker.gallery.ui.gallery

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val searchResultState = galleryViewModel.pagingDataFlow.collectAsLazyPagingItems()
    val viewModelState = galleryViewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // These two uses of LaunchedEffects are a workaround for an issue I was seeing with the list
    // state not refreshing correctly on a new search that fails. Ideally we'd just show the
    // the placeholders in the grid view and not the Toasts.
    if (searchResultState.loadState.refresh is LoadState.Error) {
        val toastText = stringResource(R.string.gallery_list_toast_first_load_error)
        LaunchedEffect(searchResultState) {
            Toast.makeText(
                context,
                toastText, Toast.LENGTH_LONG
            ).show()
        }
    }
    if (searchResultState.loadState.append is LoadState.Error) {
        val toastText = stringResource(R.string.gallery_list_toast_page_load_error)
        LaunchedEffect(searchResultState) {
            Toast.makeText(
                context,
                toastText, Toast.LENGTH_LONG
            )
                .show()
        }
    }

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
                // First load
                when (loadState.refresh) {
                    is LoadState.Error -> {
                        // Using spans here and below to show progress indicator in center of row in
                        // multi-column case
                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            GalleryListEmptyPlaceholderWithError(
                                modifier = Modifier
                                    .padding(horizontal = columnHorizontalPaddingSize)
                                    .fillMaxSize(),
                                onRetry = { searchResultState.retry() }
                            )
                        }
                    }

                    is LoadState.Loading -> {
                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            ProgressIndicator()
                        }
                    }

                    is LoadState.NotLoading -> {
                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            // Only display this when we have no items to show
                            if (itemCount == 0) {
                                GalleryListEmptyPlaceholder(
                                    modifier = Modifier
                                        .padding(horizontal = columnHorizontalPaddingSize)
                                        .fillMaxSize()
                                )
                            }
                        }
                    }
                }
                // Pagination load
                when (loadState.append) {
                    is LoadState.Error -> {
                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            PaginationFailedFooter() {
                                searchResultState.retry()
                            }
                        }
                    }

                    is LoadState.Loading -> { // Pagination Loading UI
                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                            ProgressIndicator()
                        }
                    }

                    else -> {/* No-op, we won't show anything when not loading in the append case */
                    }
                }
            }
        }
    }
}

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