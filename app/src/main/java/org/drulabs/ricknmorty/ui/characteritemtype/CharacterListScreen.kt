package org.drulabs.ricknmorty.ui.characteritemtype

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.drulabs.ricknmorty.R
import org.drulabs.ricknmorty.ui.components.SearchAppBar
import org.drulabs.ricknmorty.ui.theme.MyApplicationTheme

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun CharacterItemTypeScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = hiltViewModel(),
    onclick: (Character) -> Unit,
) {
    val uiState by viewModel.uiStatePublic.collectAsStateWithLifecycle()
    val loadingState = remember {
        mutableStateOf(uiState is CharactersUiState.Loading)
    }
    val errorState = remember {
        mutableStateOf((uiState as? CharactersUiState.Error)?.throwable)
    }
    val dataState = remember {
        mutableStateOf(
            (uiState as? CharactersUiState.Success)?.data
        )
    }
    var isSearchInProgress by remember {
        mutableStateOf(false)
    }
    when (uiState) {
        is CharactersUiState.Error -> {
            loadingState.value = false
            errorState.value = (uiState as CharactersUiState.Error).throwable
        }

        is CharactersUiState.Loading -> {
            loadingState.value = uiState is CharactersUiState.Loading
        }

        is CharactersUiState.Success -> {
            loadingState.value = false
            dataState.value = (uiState as CharactersUiState.Success).data
        }
    }

    MyTopAppBar(
        onSearch = {
            isSearchInProgress = true
            viewModel.searchCharacter(it)
        },
        onClose = {
            viewModel.refresh(shouldLoadNext = true)
            isSearchInProgress = false
        }
    )

    CharacterItemsScreen(
        data = dataState.value,
        onClick = onclick,
        loadNext = {
            if (!isSearchInProgress) {
                viewModel.loadNextPage()
            } else {
                viewModel.loadNextSearchPage()
            }
        },
        isLoading = loadingState.value,
        error = errorState.value,
        modifier = modifier
    )
}

@Composable
internal fun CharacterItemsScreen(
    data: List<Character>?,
    onClick: (Character) -> Unit,
    loadNext: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean? = null,
    error: Throwable? = null
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollState
    ) {
        itemsIndexed(
            items = data ?: emptyList(),
            key = { _, character -> character.id },
            contentType = { _, character -> character.status }
        ) { _, character ->
            CharacterItem(character = character, onClick = onClick)
        }

        item {
            if (isLoading == true) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(64.dp)
                            .height(64.dp)
                            .padding(8.dp)
                    )
                }
            } else if (error != null) {
                DisplayErrorRow()
            }
        }
    }

    scrollState.onBottomReached {
        loadNext()
    }
}

@Composable
fun LazyListState.onBottomReached(loadMore: () -> Unit) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastItem.index != 0 && lastItem.index >= layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect { shouldLoad ->
                if (shouldLoad)
                    loadMore()
            }
    }
}

@Composable
fun DisplayErrorRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
        )
        Text(text = stringResource(id = R.string.custom_error_rick_morty))
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun MyTopAppBar(
    onSearch: (String) -> Unit = {},
    onClose: () -> Unit = {}
) {
    var showSearchBar by remember { mutableStateOf(false) }
    if (showSearchBar) {
        SearchAppBar(
            query = "",
            onQueryChanged = {
                onSearch(it)
            },
            onSearch = {
                onSearch(it)
                showSearchBar = false
            },
            onCloseClicked = {
                showSearchBar = false
                onClose()
            }
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(8.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = stringResource(id = R.string.app_name)
                )
            },
            actions = {
                IconButton(onClick = {
                    showSearchBar = true
                }) {
                    Icon(
                        imageVector = Icons.TwoTone.Search,
                        contentDescription = stringResource(id = R.string.search_text)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.secondary,
                navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                scrolledContainerColor = MaterialTheme.colorScheme.tertiary,
                titleContentColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
internal fun CharacterItem(
    character: Character,
    onClick: (Character) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .clickable(true) {
                onClick(character)
            },
        shape = RoundedCornerShape(corner = CornerSize(20.dp)),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)

    ) {
        Surface(
            color = if (character.status == "Dead")
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.primary
        ) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = character.name,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.rick_morty_default),
                    error = painterResource(id = R.drawable.rick_morty_default)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = character.name,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                .wrapContentWidth()
                        )
                        Icon(
                            painter = painterResource(
                                id = when (character.gender) {
                                    "Male" -> R.drawable.gender_male_24
                                    "Female" -> R.drawable.gender_female_24
                                    else -> R.drawable.gender_unknown_24
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 2.dp,
                                    end = 2.dp
                                )
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = when (character.status) {
                                    "Dead" -> R.drawable.dead
                                    else -> R.drawable.alive
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 2.dp,
                                    end = 2.dp
                                )
                        )
                        Text(
                            text = "[${character.species}]",
                            fontSize = 20.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                .wrapContentWidth()
                        )
                    }
                }
                Text(
                    text = "Location: ${character.location.name}",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                        .wrapContentWidth()
                )
                Text(
                    text = "Aired in [${character.episodes.size}] episodes",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .wrapContentWidth()
                )
            }
        }
    }
}

// Previews
@Preview
@Composable
private fun CharacterItemPreview() {
    val character = Character(
        id = 3,
        name = "Summer Smith",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Female",
        origin = NameWithUrl(
            name = "Earth (Replacement Dimension)",
            url = "https://rickandmortyapi.com/api/location/20"
        ),
        location = NameWithUrl(
            name = "Earth (Replacement Dimension)",
            url = "https://rickandmortyapi.com/api/location/20"
        ),
        image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
        url = "https://rickandmortyapi.com/api/character/3"
    )
    CharacterItem(character, onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        val character = Character(
            id = 3,
            name = "Summer Smith Summer Smith Summer Smith Summer Smith",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Female",
            origin = NameWithUrl(
                name = "Earth (Replacement Dimension)",
                url = "https://rickandmortyapi.com/api/location/20"
            ),
            location = NameWithUrl(
                name = "Earth (Replacement Dimension)",
                url = "https://rickandmortyapi.com/api/location/20"
            ),
            image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
            url = "https://rickandmortyapi.com/api/character/3"
        )
        val characters = listOf(character, character, character, character)
        CharacterItemsScreen(
            data = characters,
            onClick = {},
            loadNext = {}
        )
    }
}