package com.example.kmmdemotest.demo.foldable

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FoldableAdapteDemo() {
    var selectedDemoName by rememberSaveable {
        mutableStateOf(FoldableAdaptiveDemo.ListDetail.name)
    }
    val selectedDemo = FoldableAdaptiveDemo.entries.firstOrNull { it.name == selectedDemoName }
        ?: FoldableAdaptiveDemo.ListDetail

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeContent.only(WindowInsetsSides.Vertical)),
        ) {
            FoldableDemoSelector(
                selectedDemo = selectedDemo,
                onDemoSelected = { selectedDemoName = it.name },
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                when (selectedDemo) {
                    FoldableAdaptiveDemo.ListDetail -> FoldableListDetailDemo()
                    FoldableAdaptiveDemo.Feed -> FoldableFeedDemo()
                    FoldableAdaptiveDemo.Files -> FoldableFilesDemo()
                    FoldableAdaptiveDemo.SupportingPane -> FoldableSupportingPaneDemo()
                    FoldableAdaptiveDemo.AdaptiveNavigation -> FoldableAdaptiveNavigationDemo()
                }
            }
        }
    }
}

@Composable
private fun FoldableDemoSelector(
    selectedDemo: FoldableAdaptiveDemo,
    onDemoSelected: (FoldableAdaptiveDemo) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FoldableAdaptiveDemo.entries.forEach { demo ->
            val buttonModifier = Modifier.widthIn(min = 120.dp)
            if (demo == selectedDemo) {
                Button(
                    onClick = { onDemoSelected(demo) },
                    modifier = buttonModifier,
                ) {
                    Text(text = demo.title)
                }
            } else {
                OutlinedButton(
                    onClick = { onDemoSelected(demo) },
                    modifier = buttonModifier,
                ) {
                    Text(text = demo.title)
                }
            }
        }
    }
}

private enum class FoldableAdaptiveDemo(val title: String) {
    ListDetail("ListDetail"),
    Feed("Feed"),
    Files("Files"),
    SupportingPane("SupportingPane"),
    AdaptiveNavigation("Navigation"),
}
