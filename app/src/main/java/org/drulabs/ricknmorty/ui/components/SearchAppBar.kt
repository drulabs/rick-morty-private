package org.drulabs.ricknmorty.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.drulabs.ricknmorty.ui.theme.MyApplicationTheme

@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    onCloseClicked: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary,
        shadowElevation = 8.dp,
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                var queryText by remember { mutableStateOf(query) }
                TextField(
                    value = queryText,
                    onValueChange = {
                        if (it.length <= 8) {
                            queryText = it
                            if (it.length >= 2) {
                                onQueryChanged(it)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onSearch(queryText)
                            keyboardController?.hide()
                        },
                    ),
                    leadingIcon = {
                        Icon(Icons.TwoTone.Search, contentDescription = "Search Icon")
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.TwoTone.Close,
                            contentDescription = "Search Icon",
                            modifier = Modifier.clickable {
                                onCloseClicked()
                            }
                        )
                    },
                    isError = queryText.length >= 8,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun SearchBarPreview() {
    MyApplicationTheme {
        SearchAppBar(query = "", onQueryChanged = {}, onSearch = {})
    }
}