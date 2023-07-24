package dev.kirstenbaker.gallery.ui.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kirstenbaker.gallery.R
import dev.kirstenbaker.gallery.ui.theme.GalleryTheme

val textTopPaddingSize = 10.dp
val textBottomPaddingSize = 30.dp

@Composable
fun GalleryListEmptyPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            contentDescription = stringResource(R.string.empty_gallery_list_image_content_description),
            painter = painterResource(id = R.drawable.frame_picture_icon),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary.copy(ContentAlpha.medium)),
        )
        Text(
            modifier = Modifier.padding(top = textTopPaddingSize, bottom = textBottomPaddingSize),
            text = stringResource(R.string.empty_gallery_list_body),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(ContentAlpha.medium)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryListEmptyPlaceholderPreview() {
    GalleryTheme {
        GalleryListEmptyPlaceholder()
    }
}