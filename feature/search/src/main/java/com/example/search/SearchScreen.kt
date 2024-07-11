package com.example.search

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.search.component.SearchTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
    modifier: Modifier = Modifier,
) {
    val repos = viewModel.pagingData.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    Column {
        ListContent(
            repos = repos,
            searchQuery = searchQuery,
            onSearchQueryChanged = viewModel::onSearchQueryChanged,
            onSearchTriggered = viewModel::onSearchTriggered,
            modifier = modifier,
        )
    }
}

@Composable
fun ListContent(
    repos: LazyPagingItems<RepoItem>,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    modifier: Modifier,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        snapshotFlow { repos.loadState }.collect { loadStates ->
            if (loadStates.refresh is LoadState.Error || loadStates.append is LoadState.Error) {
                Toast
                    .makeText(
                        context,
                        "No internet connection",
                        Toast.LENGTH_LONG,
                    ).show()
            }
        }
    }
    SearchTextField(
        onSearchQueryChanged = onSearchQueryChanged,
        onSearchTriggered = onSearchTriggered,
        searchQuery = searchQuery,
        modifier = modifier,
    )
    Box(modifier = modifier.fillMaxSize()) {
        if (repos.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            when {
                repos.itemCount > 0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                        items(count = repos.itemCount, key = { it }) {
                            repos[it]?.let { repoItem ->
                                CardRepoItem(
                                    repoItem = repoItem,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                        item {
                            if (repos.loadState.append is LoadState.Loading) {
                                CircularProgressIndicator()
                            }
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
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
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
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
        modifier = Modifier.fillMaxWidth().padding(8.dp),
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
