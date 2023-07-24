package dev.kirstenbaker.gallery.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import dev.kirstenbaker.gallery.R
import dev.kirstenbaker.gallery.data.dummy.testArtwork1
import dev.kirstenbaker.gallery.model.Artwork
import dev.kirstenbaker.gallery.model.util.ImageUrlGenerator
import dev.kirstenbaker.gallery.ui.PainterUtil.forwardingPainter
import dev.kirstenbaker.gallery.ui.ProgressIndicator
import dev.kirstenbaker.gallery.ui.theme.GalleryTheme
import dev.kirstenbaker.gallery.viewmodel.ArtworkDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworkDetailScreen(
    modifier: Modifier = Modifier,
    artworkDetailViewModel: ArtworkDetailViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val artworkState by rememberSaveable {
        artworkDetailViewModel.artworkState
    }
    val isLoadingState by rememberSaveable {
        artworkDetailViewModel.isLoading
    }
    Scaffold(
        modifier = modifier
            .wrapContentSize(),
        topBar = { ArtworkDetailTopBar(onBackPressed = { onBackPressed() }) }
    ) { paddingValues ->
        if (isLoadingState) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ProgressIndicator(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }
        } else {
            val artwork = artworkState
            if (artwork != null) {
                ArtworkDetail(
                    artwork = artwork,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(20.dp)
                )
            } else {
                ArtworkEmptyPlaceholder(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(20.dp)
                )
            }
        }
    }
}

@Composable
fun ArtworkDetail(modifier: Modifier = Modifier, artwork: Artwork) {
    val isArtworkOnDisplay = artwork.isOnView
    val scrollState = rememberScrollState()
    var isImageLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier
                    .wrapContentSize()
                    .defaultMinSize(200.dp),
                model = ImageUrlGenerator.generateDefaultSizeImageUrl(artwork.imageId.orEmpty()),
                placeholder = forwardingPainter(
                    painterResource(id = R.drawable.ic_launcher_background),
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.1f
                        )
                    )
                ),
                error = forwardingPainter(
                    painterResource(id = R.drawable.baseline_question_mark_24),
                    colorFilter = ColorFilter.tint(
                        MaterialTheme.colorScheme.onBackground.copy(
                            ContentAlpha.disabled
                        )
                    )
                ),
                contentDescription = if (!artwork.title.isNullOrEmpty()) {
                    stringResource(
                        R.string.artwork_image_content_description_with_title,
                        artwork.title
                    )
                } else {
                    stringResource(
                        R.string.artwork_image_content_description_without_title,
                    )
                },
                contentScale = ContentScale.FillWidth,
                onSuccess = {
                    isImageLoading = false
                },
                onError = {
                    isImageLoading = false
                },
                onLoading = {
                    isImageLoading = true
                },
            )
            if (isImageLoading) {
                ProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            artwork.title?.let {
                Text(
                    text = artwork.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    overflow = TextOverflow.Ellipsis
                )
            }
            artwork.dateDisplay?.let {
                Text(
                    text = artwork.dateDisplay,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    ),
                )
            }
            artwork.mediumDisplay?.let {
                Text(
                    text = artwork.mediumDisplay,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontStyle = FontStyle.Italic

                    ),
                )
            }
            artwork.artistDisplay?.let {
                Text(
                    text = artwork.artistDisplay,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(ContentAlpha.medium),
                    ),
                    overflow = TextOverflow.Ellipsis
                )
            }
            // Only show info about whether the piece is on display if we receive the boolean
            isArtworkOnDisplay?.let { isOnDisplay ->
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    textAlign = TextAlign.Center,
                    text = if (isOnDisplay) {
                        if (!artwork.galleryTitle.isNullOrEmpty()) {
                            stringResource(
                                R.string.artwork_available_display_info_with_gallery,
                                artwork.galleryTitle
                            )
                        } else {
                            stringResource(
                                R.string.artwork_available_display_info_without_gallery
                            )
                        }
                    } else {
                        stringResource(R.string.artwork_unavailable_display_info)
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworkDetailTopBar(onBackPressed: () -> Unit) {
    TopAppBar(title = {}, navigationIcon = {
        IconButton(onBackPressed) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = stringResource(R.string.back_navigation_button)
            )
        }
    })
}

@Preview(showBackground = true)
@Composable
fun ArtworkDetailPreview() {
    GalleryTheme {
        ArtworkDetail(
            artwork = testArtwork1
        )
    }
}