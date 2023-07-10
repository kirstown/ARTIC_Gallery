package dev.kirstenbaker.gallery.ui.gallery

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import dev.kirstenbaker.gallery.ui.theme.GalleryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier, queryString: String, onSearch: (String) -> Unit) {

    val focusManager = LocalFocusManager.current

    var inputValue by remember { mutableStateOf(TextFieldValue(queryString)) }
    OutlinedTextField(
        value = inputValue,
        onValueChange = { newTextFieldValue ->
            inputValue = newTextFieldValue
        },
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onSearch(inputValue.text)
        }),
        label = {
            Text(
                color = MaterialTheme.colorScheme.onBackground.copy(ContentAlpha.medium),
                text = "Search for art pieces"
            )
        },
        modifier = modifier
            .padding(bottom = cardPaddingSize, start = cardPaddingSize, end = cardPaddingSize)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        maxLines = 1,
        singleLine = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onBackground.copy(ContentAlpha.disabled),
                contentDescription = "Magnifying glass icon indicating search"
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    GalleryTheme {
        SearchBar(queryString = "", onSearch = {})
    }
}