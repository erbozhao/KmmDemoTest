package com.example.kmmdemotest.demo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private data class ReadingSection(
    val id: Int,
    val title: String,
    val body: String,
    val note: String,
    val accent: Color,
)

private val readingSections = listOf(
    ReadingSection(
        id = 1,
        title = "Adaptive reading surface",
        body = "A supporting pane keeps related material close to the document without turning the page into a dense dashboard. On compact screens, readers can move between the article and its support material. On expanded foldables, both panes can stay visible.",
        note = "Use the supporting pane for contextual notes, outline entries, references, and actions tied to the current document.",
        accent = Color(0xFF0F766E),
    ),
    ReadingSection(
        id = 2,
        title = "Compact navigation",
        body = "When the window only has room for one pane, the main document remains the primary destination. Readers open the supporting pane when they need outline or reference material, then navigate back to the article.",
        note = "Compact width should avoid permanently splitting the content area.",
        accent = Color(0xFF2563EB),
    ),
    ReadingSection(
        id = 3,
        title = "Expanded posture",
        body = "On a larger unfolded screen, the layout can preserve the article while presenting notes on the side. This makes the relationship between reading content and supporting context visible at a glance.",
        note = "The navigator and scaffold value decide whether panes are shown together or adapted.",
        accent = Color(0xFF7C3AED),
    ),
)

private val readingResources = listOf(
    "Canonical layouts",
    "Supporting pane guidance",
    "Window size classes",
    "Foldable posture",
)

private val readingSettings = listOf(
    "Reading width: comfortable",
    "Line height: relaxed",
    "Theme: system",
    "Sync notes: enabled",
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
actual fun FoldableSupportingPaneDemo() {
    var selectedSectionId by remember { mutableIntStateOf(readingSections.first().id) }
    val selectedSection = readingSections.first { it.id == selectedSectionId }
    val windowAdaptiveInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val scaffoldDirective =
        calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(windowAdaptiveInfo)
    val showSupportingPane = scaffoldDirective.maxHorizontalPartitions >= 2
    val showExtraPane = scaffoldDirective.maxHorizontalPartitions >= 3
    val initialHistory = remember(showSupportingPane, showExtraPane) {
        buildList {
            add(ThreePaneScaffoldDestinationItem(SupportingPaneScaffoldRole.Main, "main"))
            if (showSupportingPane) {
                add(
                    ThreePaneScaffoldDestinationItem(
                        pane = SupportingPaneScaffoldRole.Supporting,
                        contentKey = "supporting",
                    )
                )
            }
            if (showExtraPane) {
                add(
                    ThreePaneScaffoldDestinationItem(
                        pane = SupportingPaneScaffoldRole.Extra,
                        contentKey = "extra",
                    )
                )
            }
        }
    }
    val navigator = rememberSupportingPaneScaffoldNavigator<String>(
        scaffoldDirective = scaffoldDirective,
        initialDestinationHistory = initialHistory,
    )
    val scope = rememberCoroutineScope()

    if (!showSupportingPane) {
        DocumentPane(
            selectedSection = selectedSection,
            onOpenSupportingPane = {},
        )
        return
    }

    LaunchedEffect(showSupportingPane, showExtraPane) {
        if (showExtraPane) {
            navigator.navigateTo(
                pane = SupportingPaneScaffoldRole.Extra,
                contentKey = "extra",
            )
        } else if (showSupportingPane) {
            navigator.navigateTo(
                pane = SupportingPaneScaffoldRole.Supporting,
                contentKey = "supporting",
            )
        } else if (navigator.currentDestination?.pane != SupportingPaneScaffoldRole.Main) {
            navigator.navigateTo(
                pane = SupportingPaneScaffoldRole.Main,
                contentKey = "main",
            )
        }
    }

    SupportingPaneScaffold(
        directive = navigator.scaffoldDirective,
        scaffoldState = navigator.scaffoldState,
        mainPane = {
            AnimatedPane {
                DocumentPane(
                    selectedSection = selectedSection,
                    onOpenSupportingPane = {
                        scope.launch {
                            navigator.navigateTo(
                                pane = SupportingPaneScaffoldRole.Supporting,
                                contentKey = "supporting",
                            )
                        }
                    },
                )
            }
        },
        supportingPane = {
            AnimatedPane {
                ReadingSupportPane(
                    selectedSectionId = selectedSectionId,
                    selectedSection = selectedSection,
                    canNavigateBack = !showSupportingPane && navigator.canNavigateBack(),
                    onBack = {
                        scope.launch {
                            navigator.navigateBack()
                        }
                    },
                    onSectionSelected = { section ->
                        selectedSectionId = section.id
                        if (!showSupportingPane) {
                            scope.launch {
                                navigator.navigateTo(
                                    pane = SupportingPaneScaffoldRole.Main,
                                    contentKey = "main",
                                )
                            }
                        }
                    },
                )
            }
        },
        extraPane = {
            AnimatedPane {
                ReadingExtraPane(selectedSection = selectedSection)
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun DocumentPane(
    selectedSection: ReadingSection,
    onOpenSupportingPane: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Foldable Reading",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Main pane",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        SectionMarker(section = selectedSection)
                        Text(
                            text = selectedSection.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = selectedSection.body,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        OutlinedButton(onClick = onOpenSupportingPane) {
                            Text("Open notes")
                        }
                    }
                }
            }
            items(readingSections, key = { it.id }) { section ->
                DocumentSectionPreview(
                    section = section,
                    selected = section.id == selectedSection.id,
                )
            }
        }
    }
}

@Composable
private fun DocumentSectionPreview(
    section: ReadingSection,
    selected: Boolean,
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(section.accent),
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = section.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ReadingSupportPane(
    selectedSectionId: Int,
    selectedSection: ReadingSection,
    canNavigateBack: Boolean,
    onBack: () -> Unit,
    onSectionSelected: (ReadingSection) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                if (canNavigateBack) {
                    OutlinedButton(onClick = onBack) {
                        Text("Back to article")
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = "Notes and outline",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = selectedSection.note,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            HorizontalDivider()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Text(
                        text = "Outline",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    )
                }
                items(readingSections, key = { it.id }) { section ->
                    SupportSectionRow(
                        section = section,
                        selected = section.id == selectedSectionId,
                        onClick = { onSectionSelected(section) },
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 20.dp))
                }
                item {
                    Text(
                        text = "Resources",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                    )
                }
                items(readingResources) { resource ->
                    Text(
                        text = resource,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReadingExtraPane(
    selectedSection: ReadingSection,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Reader tools",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Extra pane",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = "Document info",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = "Current section: ${selectedSection.title}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "3-pane mode keeps article, notes, and tools visible.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            item {
                Text(
                    text = "Reading settings",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            items(readingSettings) { setting ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                ) {
                    Text(
                        text = setting,
                        modifier = Modifier.padding(14.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupportSectionRow(
    section: ReadingSection,
    selected: Boolean,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = section.title,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = section.note,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingContent = {
            SectionMarker(section = section)
        },
        colors = ListItemDefaults.colors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                Color.Transparent
            },
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
private fun SectionMarker(section: ReadingSection) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(section.accent),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = section.id.toString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
}
