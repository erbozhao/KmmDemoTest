package com.example.kmmdemotest.demo.foldable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

private enum class AdaptiveNavigationDestination(
    val label: String,
    val iconText: String,
    val title: String,
    val description: String,
    val accent: Color,
) {
    Home(
        label = "Home",
        iconText = "H",
        title = "Adaptive home",
        description = "The navigation component changes placement as the available window width grows.",
        accent = Color(0xFF006C67),
    ),
    Messages(
        label = "Messages",
        iconText = "M",
        title = "Inbox focus",
        description = "Compact screens keep navigation close to the thumb while wider screens reserve persistent navigation space.",
        accent = Color(0xFF3F5EBA),
    ),
    Tasks(
        label = "Tasks",
        iconText = "T",
        title = "Work queue",
        description = "NavigationSuiteScaffold lets the destination model stay stable while the chrome adapts.",
        accent = Color(0xFF9B4D00),
    ),
    Settings(
        label = "Settings",
        iconText = "S",
        title = "Preferences",
        description = "Foldables and large screens can expose more navigation context without changing content state.",
        accent = Color(0xFF7C3AED),
    ),
}

private data class NavigationDemoItem(
    val id: Int,
    val title: String,
    val body: String,
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
actual fun FoldableAdaptiveNavigationDemo() {
    var selectedDestination by remember { mutableStateOf(AdaptiveNavigationDestination.Home) }
    val windowAdaptiveInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val windowSummary = windowAdaptiveInfo.windowSizeClass.widthDescription()
    val showNavigationSuite =
        windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
            WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
        )
    val navigationSuiteState = rememberNavigationSuiteScaffoldState(
        initialValue = if (showNavigationSuite) {
            NavigationSuiteScaffoldValue.Visible
        } else {
            NavigationSuiteScaffoldValue.Hidden
        }
    )

    LaunchedEffect(showNavigationSuite) {
        if (showNavigationSuite) {
            navigationSuiteState.show()
        } else {
            navigationSuiteState.hide()
        }
    }

    NavigationSuiteScaffold(
        state = navigationSuiteState,
        navigationSuiteItems = {
            AdaptiveNavigationDestination.entries.forEach { destination ->
                item(
                    selected = destination == selectedDestination,
                    onClick = { selectedDestination = destination },
                    icon = {
                        DestinationIcon(destination = destination)
                    },
                    label = { Text(destination.label) },
                )
            }
        },
    ) {
        AdaptiveNavigationContent(
            destination = selectedDestination,
            windowSummary = windowSummary,
        )
    }
}

@Composable
private fun AdaptiveNavigationContent(
    destination: AdaptiveNavigationDestination,
    windowSummary: String,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                NavigationHeader(
                    destination = destination,
                    windowSummary = windowSummary,
                )
            }
            items(destination.demoItems(), key = { it.id }) { item ->
                NavigationContentCard(
                    item = item,
                    accent = destination.accent,
                )
            }
        }
    }
}

@Composable
private fun NavigationHeader(
    destination: AdaptiveNavigationDestination,
    windowSummary: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DestinationIcon(destination = destination)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = destination.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = destination.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Text(
                text = "Window width: $windowSummary",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun DestinationIcon(destination: AdaptiveNavigationDestination) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = destination.iconText,
            color = destination.accent,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun NavigationContentCard(
    item: NavigationDemoItem,
    accent: Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = accent,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun AdaptiveNavigationDestination.demoItems(): List<NavigationDemoItem> = when (this) {
    AdaptiveNavigationDestination.Home -> listOf(
        NavigationDemoItem(1, "Compact", "Bottom navigation is preferred when horizontal space is constrained."),
        NavigationDemoItem(2, "Medium", "Navigation can move to a rail so content keeps more vertical room."),
        NavigationDemoItem(3, "Expanded", "Larger windows can keep destinations visible with a wider navigation surface."),
    )
    AdaptiveNavigationDestination.Messages -> listOf(
        NavigationDemoItem(1, "Unread", "The current destination state remains stable when the device is folded or resized."),
        NavigationDemoItem(2, "Pinned", "NavigationSuiteScaffold owns the navigation chrome while content stays destination-driven."),
        NavigationDemoItem(3, "Archive", "The same destination list can serve phones, tablets, and foldables."),
    )
    AdaptiveNavigationDestination.Tasks -> listOf(
        NavigationDemoItem(1, "Today", "Task content is independent from the navigation component shape."),
        NavigationDemoItem(2, "Upcoming", "Window size changes should not reset the selected destination."),
        NavigationDemoItem(3, "Done", "A single scaffold can adapt without separate phone and tablet screens."),
    )
    AdaptiveNavigationDestination.Settings -> listOf(
        NavigationDemoItem(1, "Display", "Adaptive navigation helps preferences stay reachable on large screens."),
        NavigationDemoItem(2, "Sync", "The demo avoids business data and focuses on navigation behavior."),
        NavigationDemoItem(3, "About", "The official component chooses the appropriate navigation type for the window."),
    )
}

private fun WindowSizeClass.widthDescription(): String = when {
    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> "expanded"
    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> "medium"
    else -> "compact"
}
