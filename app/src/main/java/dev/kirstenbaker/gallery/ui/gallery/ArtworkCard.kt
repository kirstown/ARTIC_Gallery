package dev.kirstenbaker.gallery.ui.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.kirstenbaker.gallery.R
import dev.kirstenbaker.gallery.data.dummy.testArtwork1
import dev.kirstenbaker.gallery.model.Artwork
import dev.kirstenbaker.gallery.model.util.ImageUrlGenerator
import dev.kirstenbaker.gallery.ui.PainterUtil
import dev.kirstenbaker.gallery.ui.theme.GalleryTheme

val cardPaddingSize = 10.dp

@Composable
fun ArtworkCard(modifier: Modifier = Modifier, artwork: Artwork, onClickArtwork: (Int) -> Unit) {
    OutlinedCard(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = cardPaddingSize, start = cardPaddingSize, end = cardPaddingSize)
            .height(120.dp)
            .clickable { onClickArtwork(artwork.id) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPaddingSize),
        ) {
            // Image caching is enabled by default when creating a Coil Painter object
            Box(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    model = ImageUrlGenerator.generateThumbnailUrl(artwork.imageId.orEmpty()),
                    placeholder = PainterUtil.forwardingPainter(
                        painterResource(id = R.drawable.frame_picture_icon),
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.onBackground.copy(
                                ContentAlpha.disabled
                            )
                        )
                    ),
                    error = PainterUtil.forwardingPainter(
                        painterResource(id = R.drawable.frame_picture_icon),
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
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = cardPaddingSize)
                    .fillMaxHeight(),
            ) {
                artwork.title?.let {
                    Text(
                        text = artwork.title,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (artwork.artistTitle != null) {
                    Text(
                        text = artwork.artistTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else if (artwork.placeOfOrigin != null) {
                    // If artist title isn't available, show artwork's place of origin
                    Text(
                        text = artwork.placeOfOrigin,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtworkCardPreview() {
    GalleryTheme {
        ArtworkCard(
            artwork = testArtwork1.copy(
                title = "Extremely long Artwork Title that shows two lines",
                artistDisplay = "Long Artist Name",
                dateDisplay = "Sometime in the 1500s"
            ),
            onClickArtwork = { })
    }
}