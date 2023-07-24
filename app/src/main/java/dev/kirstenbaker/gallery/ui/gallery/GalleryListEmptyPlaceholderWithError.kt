package dev.kirstenbaker.gallery.ui.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.kirstenbaker.gallery.R
import dev.kirstenbaker.gallery.ui.theme.GalleryTheme

@Composable
fun GalleryListEmptyPlaceholderWithError(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        GalleryListEmptyPlaceholder()
        Text(
            modifier = Modifier.padding(
                top = textTopPaddingSize,
            ),
            text = stringResource(R.string.first_load_failed_body),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(ContentAlpha.disabled)
        )
        TextButton(onClick = { onRetry() }) {
            Text(
                text = stringResource(R.string.first_load_failed_retry_cta),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GalleryListEmptyPlaceholderWithErrorPreview() {
    GalleryTheme {
        GalleryListEmptyPlaceholderWithError(onRetry = { })
    }
}