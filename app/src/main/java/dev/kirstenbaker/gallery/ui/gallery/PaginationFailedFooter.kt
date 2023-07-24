package dev.kirstenbaker.gallery.ui.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.kirstenbaker.gallery.R
import dev.kirstenbaker.gallery.ui.theme.GalleryTheme

@Composable
fun PaginationFailedFooter(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(vertical = textTopPaddingSize),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.page_load_failed_body),
            color = MaterialTheme.colorScheme.onBackground.copy(ContentAlpha.disabled)
        )
        TextButton(onClick = { onRetry() }) {
            Text(stringResource(R.string.page_load_failed_retry_cta), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaginationFailedFooterPreview() {
    GalleryTheme {
        PaginationFailedFooter(onRetry = { })
    }
}