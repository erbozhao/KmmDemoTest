package com.example.kmmdemotest.demo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private data class FallbackMail(
    val id: Int,
    val title: String,
    val preview: String,
)

private val fallbackMails = listOf(
    FallbackMail(1, "Foldable layout review", "The list-detail scaffold should keep navigation predictable."),
    FallbackMail(2, "Canonical layout guidance", "List-detail is recommended for collections with item details."),
    FallbackMail(3, "Portrait behavior", "The initial compact screen must only display the list pane."),
    FallbackMail(4, "Unfolded behavior", "Unfolded devices should make better use of horizontal space."),
)

@Composable
actual fun FoldableListDetailDemo() {
    var selectedId by remember { mutableIntStateOf(fallbackMails.first().id) }
    val selectedMail = fallbackMails.first { it.id == selectedId }

    Row(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
        ) {
            items(fallbackMails, key = { it.id }) { mail ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedId = mail.id }
                        .padding(16.dp),
                ) {
                    Text(
                        text = mail.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (mail.id == selectedId) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        },
                    )
                    Text(
                        text = mail.preview,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                HorizontalDivider()
            }
        }
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = selectedMail.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = selectedMail.preview,
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

