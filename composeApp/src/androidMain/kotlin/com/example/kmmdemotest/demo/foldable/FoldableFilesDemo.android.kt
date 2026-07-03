package com.example.kmmdemotest.demo.foldable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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

private data class FileCategory(
    val name: String,
    val count: String,
    val color: Color,
    val symbol: String,
)

private data class RecentFile(
    val type: String,
    val name: String,
    val source: String,
    val color: Color,
)

private data class CleanItem(
    val name: String,
    val size: String,
    val color: Color,
    val symbol: String,
)

private val fileCategories = listOf(
    FileCategory("Storage", "580", Color(0xFF3B82F6), "S"),
    FileCategory("Videos", "10", Color(0xFF7C3AED), "V"),
    FileCategory("Music", "32", Color(0xFFF43F5E), "M"),
    FileCategory("Documents", "36", Color(0xFFF59E0B), "D"),
    FileCategory("Image", "900", Color(0xFF0EA5E9), "I"),
    FileCategory("Archives", "12", Color(0xFF4F46E5), "A"),
    FileCategory("WhatsApp", "10", Color(0xFF22C55E), "W"),
    FileCategory("More", "10", Color(0xFF64748B), "+"),
)

private val recentFiles = listOf(
    RecentFile("PDF", "CabinaArmadio_doppia.pdf", "WhatsApp Opened May 23, 2021", Color(0xFFFF4F3F)),
    RecentFile("EXL", "Middle East North Africa Internet Infrastructure 2020-EN.exl", "WhatsApp Opened May 23, 2021", Color(0xFF42CE36)),
)

private val cleanItems = listOf(
    CleanItem("Videos", "Clean 23.5 GB", Color(0xFF7C3AED), "V"),
    CleanItem("WhatsApp", "Clean 420.5 MB", Color(0xFF22C55E), "W"),
    CleanItem("Browsing data", "Clean 86.3 MB", Color(0xFF4F46E5), "B"),
    CleanItem("Large files", "Clean 5.2 GB", Color(0xFFF59E0B), "L"),
)

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
actual fun FoldableFilesDemo() {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val expanded = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND,
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC),
    ) {
        if (expanded) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    item { FilesHeader() }
                    item { QuickActionRow() }
                    item { CategoryGrid() }
                    item { RecentFilesCard() }
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(top = 66.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    item { CleanCard(expanded = true) }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item { FilesHeader() }
                item { QuickActionRow() }
                item { CategoryGrid() }
                item { RecentFilesCard() }
                item { CleanCard(expanded = false) }
            }
        }
    }
}

@Composable
private fun FilesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Files",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(text = "Q", fontWeight = FontWeight.SemiBold, color = Color(0xFF374151))
            Text(text = "⋮", fontWeight = FontWeight.Bold, color = Color(0xFF374151))
        }
    }
}

@Composable
private fun QuickActionRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        QuickActionCard(
            title = "Downloads",
            subtitle = "Easy & Fast",
            accent = Color(0xFF7C3AED),
            trailing = "↓",
            modifier = Modifier.weight(1f),
        )
        QuickActionCard(
            title = "PDF Tools",
            subtitle = "",
            accent = Color(0xFFFF4F3F),
            trailing = "PDF",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    accent: Color,
    trailing: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.height(58.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.12f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = accent,
                    maxLines = 2,
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF6B7280),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(if (trailing == "PDF") 30.dp else 34.dp)
                    .clip(if (trailing == "PDF") RoundedCornerShape(9.dp) else CircleShape)
                    .background(accent),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = trailing,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun CategoryGrid() {
    SectionCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            fileCategories.chunked(4).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    rowItems.forEach { category ->
                        CategoryCell(category = category, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCell(
    category: FileCategory,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(category.color),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = category.symbol,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111827),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = category.count,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF6B7280),
        )
    }
}

@Composable
private fun RecentFilesCard() {
    SectionCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionTitle(title = "Recent", action = "More ›")
            recentFiles.forEach { file ->
                RecentFileRow(file = file)
            }
        }
    }
}

@Composable
private fun RecentFileRow(file: RecentFile) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(file.color),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = file.type,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = file.source,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF9CA3AF),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun CleanCard(expanded: Boolean) {
    SectionCard {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "538",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5),
                        )
                        Text(
                            text = " KB",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5),
                        )
                    }
                    Text(
                        text = "Junk files",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827),
                    )
                    Text(
                        text = "Phone gets frozen by junk files",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9CA3AF),
                    )
                }
                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF4F46E5))
                        .padding(horizontal = 22.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Clean",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
            if (expanded) {
                cleanItems.forEach { item ->
                    CleanItemRow(item = item)
                }
            } else {
                cleanItems.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        rowItems.forEach { item ->
                            CleanItemRow(item = item, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CleanItemRow(
    item: CleanItem,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(item.color),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = item.symbol,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.size,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B7280),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(modifier = Modifier.padding(14.dp)) {
            content()
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    action: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
        )
        Text(
            text = action,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151),
        )
    }
}
