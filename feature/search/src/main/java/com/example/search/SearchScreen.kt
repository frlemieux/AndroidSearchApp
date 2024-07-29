package com.example.search

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.search.component.SearchTextField

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel<SearchViewModel>(),
) {
    val repos = viewModel.pagingData.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    Column {
        ListContent(
            lazyPagingItems = repos,
            searchQuery = searchQuery,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onSearchTriggered = viewModel::onSearchTriggered,
            modifier = modifier,
        )
    }
}

@Composable
fun ListContent(
    lazyPagingItems: LazyPagingItems<RepoItem>,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    modifier: Modifier,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        snapshotFlow { lazyPagingItems.loadState }.collect { loadStates ->
            when (loadStates.refresh) {
                is LoadState.Error -> {
                    Toast
                        .makeText(
                            context,
                            (lazyPagingItems.loadState.refresh as LoadState.Error).error.message.toString(),
                            Toast.LENGTH_LONG,
                        ).show()
                }

                else -> Unit
            }
        }
    }
    SearchTextField(
        onSearchQueryChanged = onSearchQueryChanged,
        onSearchTriggered = onSearchTriggered,
        searchQuery = searchQuery,
        modifier = modifier,
    )
    LazyListSearch(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
    )
}

@Composable
private fun LazyListSearch(
    modifier: Modifier,
    lazyPagingItems: LazyPagingItems<RepoItem>,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            when {
                lazyPagingItems.itemCount > 0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(vertical = 16.dp),
                    ) {
                        items(count = lazyPagingItems.itemCount, key = { it }) {
                            lazyPagingItems[it]?.let { repoItem ->
                                CardRepoItem(
                                    repoItem = repoItem,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                        item {
                            if (lazyPagingItems.loadState.append is LoadState.Loading) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                else -> {
                    EmptyContent()
                }
            }
        }
    }
}

@Composable
fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Results Found",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun CardRepoItem(
    repoItem: RepoItem,
    modifier: Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = repoItem.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = repoItem.fullName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(8.dp))
            repoItem.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "⭐ ${repoItem.stars}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "🍴 ${repoItem.forks}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                repoItem.language?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GitHubRepositoryCardPreview() {
    val repoItem =
        RepoItem(
            id = 1,
            name = "compose-samples",
            fullName = "android/compose-samples",
            description = "Official Jetpack Compose samples.",
            url = "https://github.com/android/compose-samples",
            stars = 5000,
            forks = 1000,
            language = "Kotlin",
        )
    MaterialTheme {
        CardRepoItem(repoItem = repoItem, modifier = Modifier.padding(16.dp))
    }
}
