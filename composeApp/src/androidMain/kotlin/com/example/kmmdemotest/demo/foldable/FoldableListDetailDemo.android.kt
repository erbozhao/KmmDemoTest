package com.example.kmmdemotest.demo.foldable

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
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
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
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import kotlinx.coroutines.launch

private data class AdaptiveMail(
    val id: Int,
    val sender: String,
    val title: String,
    val preview: String,
    val body: String,
    val accent: Color,
)

private val adaptiveMails = listOf(
    AdaptiveMail(
        id = 1,
        sender = "Design Team",
        title = "Foldable layout review",
        preview = "The list-detail scaffold should keep navigation predictable.",
        body = "Use a compact single-pane list for portrait phones. On medium and expanded widths, keep the list visible and render the selected message in the detail pane.",
        accent = Color(0xFF0F766E),
    ),
    AdaptiveMail(
        id = 2,
        sender = "Android Docs",
        title = "Canonical layout guidance",
        preview = "List-detail is recommended for collections with item details.",
        body = "Canonical layouts help apps adapt across phones, tablets, Chromebooks, and foldables. The list-detail pattern is a good fit when users browse a collection and inspect one item.",
        accent = Color(0xFF2563EB),
    ),
    AdaptiveMail(
        id = 3,
        sender = "QA",
        title = "Portrait behavior",
        preview = "The initial compact screen must only display the list pane.",
        body = "In compact width, the detail pane should be reached only after selecting a row. Back navigation returns to the list through the scaffold navigator.",
        accent = Color(0xFF9333EA),
    ),
    AdaptiveMail(
        id = 4,
        sender = "Product",
        title = "Unfolded behavior",
        preview = "Unfolded devices should make better use of horizontal space.",
        body = "When the app has medium or expanded width, initialize the scaffold with both list and detail destinations so the first selected message appears immediately on the right.",
        accent = Color(0xFFB45309),
    ),
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
actual fun FoldableListDetailDemo() {
    var selectedId by remember { mutableIntStateOf(adaptiveMails.first().id) }
    val selectedMail = adaptiveMails.first { it.id == selectedId }
    val windowAdaptiveInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val showExtraPane = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val baseScaffoldDirective =
        calculatePaneScaffoldDirectiveWithTwoPanesOnMediumWidth(windowAdaptiveInfo)
    val scaffoldDirective = if (showExtraPane) {
        baseScaffoldDirective.copy(maxHorizontalPartitions = 3)
    } else {
        baseScaffoldDirective
    }
    val showListAndDetail = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )
    val initialHistory = remember(showListAndDetail, showExtraPane) {
        buildList {
            add(ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List))
            if (showListAndDetail) {
                add(
                    ThreePaneScaffoldDestinationItem(
                        pane = ListDetailPaneScaffoldRole.Detail,
                        contentKey = selectedId,
                    )
                )
            }
            if (showExtraPane) {
                add(
                    ThreePaneScaffoldDestinationItem(
                        pane = ListDetailPaneScaffoldRole.Extra,
                        contentKey = selectedId,
                    )
                )
            }
        }
    }
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>(
        scaffoldDirective = scaffoldDirective,
        initialDestinationHistory = initialHistory,
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(showListAndDetail, showExtraPane) {
        if (showExtraPane) {
            navigator.navigateTo(
                pane = ListDetailPaneScaffoldRole.Extra,
                contentKey = selectedId,
            )
        } else if (showListAndDetail) {
            navigator.navigateTo(
                pane = ListDetailPaneScaffoldRole.Detail,
                contentKey = selectedId,
            )
        } else if (navigator.currentDestination?.pane != ListDetailPaneScaffoldRole.List) {
            navigator.navigateTo(
                pane = ListDetailPaneScaffoldRole.List,
                contentKey = null,
            )
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                MailListPane(
                    selectedId = selectedId,
                    onMailClick = { mail ->
                        selectedId = mail.id
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = mail.id,
                            )
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                MailDetailPane(
                    mail = selectedMail,
                    canNavigateBack = navigator.canNavigateBack(),
                    onBack = {
                        scope.launch {
                            navigator.navigateBack()
                        }
                    },
                )
            }
        },
        extraPane = {
            AnimatedPane {
                MailExtraPane(mail = selectedMail)
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun MailListPane(
    selectedId: Int,
    onMailClick: (AdaptiveMail) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column {
            Text(
                text = "Inbox",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(adaptiveMails, key = { it.id }) { mail ->
                    MailRow(
                        mail = mail,
                        selected = mail.id == selectedId,
                        onClick = { onMailClick(mail) },
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 72.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MailRow(
    mail: AdaptiveMail,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        Color.Transparent
    }

    ListItem(
        headlineContent = {
            Text(
                text = mail.title,
                fontWeight = FontWeight.SemiBold,
            )
        },
        supportingContent = {
            Text(text = "${mail.sender} - ${mail.preview}")
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(mail.accent),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = mail.sender.first().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = containerColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
private fun MailDetailPane(
    mail: AdaptiveMail,
    canNavigateBack: Boolean,
    onBack: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            if (canNavigateBack) {
                OutlinedButton(onClick = onBack) {
                    Text("Back")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(mail.accent),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = mail.sender.first().toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Column {
                            Text(
                                text = mail.sender,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "Adaptive demo message",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    Text(
                        text = mail.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = mail.body,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun MailExtraPane(mail: AdaptiveMail) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Message info",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            InfoCard(
                title = "Sender",
                content = mail.sender,
            )
            InfoCard(
                title = "Layout role",
                content = "Extra pane for supporting metadata and actions.",
            )
            InfoCard(
                title = "Adaptive behavior",
                content = "Compact: list only\nMedium: list + detail\nExpanded: list + detail + extra",
            )
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
