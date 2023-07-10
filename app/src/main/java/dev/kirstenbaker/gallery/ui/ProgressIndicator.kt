package dev.kirstenbaker.gallery.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.kirstenbaker.gallery.ui.theme.GalleryTheme

@Composable
fun ProgressIndicator(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

// Not an especially helpful preview
@Preview(showBackground = true)
@Composable
fun ProgressINdicatorPreview() {
    GalleryTheme {
        ProgressIndicator()
    }
}