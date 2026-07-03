package com.example.kmmdemotest.demo.foldable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

private const val WIDTH_DP_LARGE_LOWER_BOUND = 1200
private const val WIDTH_DP_XLARGE_LOWER_BOUND = 1600

private data class AdaptiveFeedItem(
    val id: Int,
    val title: String,
    val category: String,
    val summary: String,
    val readTime: String,
    val accent: Color,
    val featured: Boolean = false,
)

private val adaptiveFeedItems = listOf(
    AdaptiveFeedItem(
        id = 1,
        title = "Foldable feeds need hierarchy",
        category = "Featured",
        summary = "A feed layout uses the available width to move from one compact column into a richer multi-column surface.",
        readTime = "6 min",
        accent = Color(0xFF006C67),
        featured = true,
    ),
    AdaptiveFeedItem(
        id = 2,
        title = "Compact phones",
        category = "Compact",
        summary = "Cards remain in a single vertical stream for comfortable scanning.",
        readTime = "3 min",
        accent = Color(0xFF3F5EBA),
    ),
    AdaptiveFeedItem(
        id = 3,
        title = "Medium width",
        category = "Tablet",
        summary = "Two columns keep more stories visible without changing the reading model.",
        readTime = "4 min",
        accent = Color(0xFF9B4D00),
    ),
    AdaptiveFeedItem(
        id = 4,
        title = "Expanded foldables",
        category = "Foldable",
        summary = "Three or more columns make unfolded screens feel intentionally designed.",
        readTime = "5 min",
        accent = Color(0xFF7C3AED),
        featured = true,
    ),
    AdaptiveFeedItem(
        id = 5,
        title = "Window size classes",
        category = "Android",
        summary = "Width classes provide a stable way to choose the right feed density.",
        readTime = "4 min",
        accent = Color(0xFFB42318),
    ),
    AdaptiveFeedItem(
        id = 6,
        title = "Visual rhythm",
        category = "Design",
        summary = "Featured cards span wider tracks while regular cards keep the grid predictable.",
        readTime = "2 min",
        accent = Color(0xFF2563EB),
    ),
    AdaptiveFeedItem(
        id = 7,
        title = "Large screens",
        category = "Desktop",
        summary = "Larger windows can show denser content while preserving readable card widths.",
        readTime = "7 min",
        accent = Color(0xFF0F766E),
    ),
    AdaptiveFeedItem(
        id = 8,
        title = "Canonical layout",
        category = "Docs",
        summary = "Feed is a canonical layout for browsing many peer content items.",
        readTime = "3 min",
        accent = Color(0xFF6D5BD0),
    ),
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
actual fun FoldableFeedDemo() {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val columns = windowAdaptiveInfo.windowSizeClass.feedColumnCount()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item(span = { GridItemSpan(columns) }) {
                FeedHeader(columns = columns)
            }
            items(
                items = adaptiveFeedItems,
                key = { it.id },
                span = { item ->
                    val span = if (item.featured) {
                        minOf(2, columns)
                    } else {
                        1
                    }
                    GridItemSpan(span)
                },
            ) { item ->
                FeedCard(
                    item = item,
                    expanded = item.featured && columns > 1,
                )
            }
        }
    }
}

private fun WindowSizeClass.feedColumnCount(): Int = when {
    isWidthAtLeastBreakpoint(WIDTH_DP_XLARGE_LOWER_BOUND) -> 4
    isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND) -> 4
    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> 3
    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> 2
    else -> 1
}

@Composable
private fun FeedHeader(columns: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Adaptive Feed",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Current grid: $columns column${if (columns == 1) "" else "s"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun FeedCard(
    item: AdaptiveFeedItem,
    expanded: Boolean,
) {
    val shape = RoundedCornerShape(8.dp)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (expanded) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
    ) {
        Column(
            modifier = Modifier.padding(if (expanded) 22.dp else 16.dp),
            verticalArrangement = Arrangement.spacedBy(if (expanded) 14.dp else 10.dp),
        ) {
            FeedCardTopRow(item = item)
            Text(
                text = item.title,
                style = if (expanded) {
                    MaterialTheme.typography.headlineSmall
                } else {
                    MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.SemiBold,
                maxLines = if (expanded) 2 else 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = if (expanded) 4 else 3,
                overflow = TextOverflow.Ellipsis,
            )
            if (expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp)
                        .clip(shape)
                        .background(item.accent.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = "Featured story",
                        modifier = Modifier.padding(horizontal = 18.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = item.accent,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedCardTopRow(item: AdaptiveFeedItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(item.accent),
            )
            Text(
                text = item.category,
                style = MaterialTheme.typography.labelLarge,
                color = item.accent,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Text(
            text = item.readTime,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
