package com.example.kmmdemotest.demo

// commonMain/kotlin/ComposePerfDemoScreen.kt
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutBoundsHolder
import androidx.compose.ui.layout.layoutBounds
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.unit.dp
import kotlin.math.sin

data class DemoItem(
    val id: Int,
    val title: String,
    val tags: List<String>,
)

@Composable
fun LagPageView2() {
    val list = remember {
        mutableStateListOf<DemoItem>().apply {
            repeat(500) { index ->
                add(
                    DemoItem(
                        id = index,
                        title = "Complex item #$index",
                        tags = List(10) { "tag-${index % 7}-$it" },
                    )
                )
            }
        }
    }

    var visibilityEvents by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()
    val viewport = remember { LayoutBoundsHolder() }

    FrameJankLogger(tag = "ComposePerfDemo")

    Column(Modifier.fillMaxSize().background(Color(0xFFF7F7F8))) {
        Row(
            Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = { list.shuffle() }) {
                Text("Shuffle")
            }
            Button(onClick = { list.reverse() }) {
                Text("Reverse")
            }
            Text(
                text = "visible events: $visibilityEvents",
                modifier = Modifier.padding(top = 12.dp),
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .layoutBounds(viewport),
        ) {
            items(
                items = list,
                key = { it.id },
            ) { item ->
                ComplexFeedItem(
                    item = item,
                    modifier = Modifier.onVisibilityChanged(
                        minDurationMs = 300,
                        minFractionVisible = 0.5f,
                        viewportBounds = viewport,
                    ) { visible ->
                        if (visible) visibilityEvents++
                    }
                )
            }
        }
    }
}

@Composable
private fun ComplexFeedItem(
    item: DemoItem,
    modifier: Modifier = Modifier,
) {
    RecomposeLogger("item-${item.id}")

    val transition = rememberInfiniteTransition(label = "item-animation")
    val animated by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "progress",
    )

    Column(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(Color.White, MaterialTheme.shapes.medium)
            .padding(12.dp)
    ) {
        Row {
            AnimatedAvatar(animated)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    "This row intentionally has nested composition, draw work, tags, and animation.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF555B66),
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(Modifier.horizontalScroll(rememberScrollState())) {
            item.tags.forEach { tag ->
                FilterChip(
                    selected = false,
                    onClick = {},
                    label = { Text(tag) },
                    modifier = Modifier.padding(end = 6.dp),
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        ExpensiveDecorativeDraw(animated)
    }
}

@Composable
private fun AnimatedAvatar(progress: Float) {
    Box(
        Modifier
            .size(48.dp)
            .graphicsLayer {
                rotationZ = progress * 360f
                scaleX = 0.92f + progress * 0.12f
                scaleY = 0.92f + progress * 0.12f
            }
            .background(Color(0xFF3B82F6), MaterialTheme.shapes.large)
    )
}

@Composable
private fun ExpensiveDecorativeDraw(progress: Float) {
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .drawWithContent { drawContent() }
    ) {
        repeat(36) { i ->
            val x = size.width * i / 35f
            val y = size.height / 2f + sin(i + progress * 6.28f) * 18f
            drawCircle(
                color = Color(
                    red = 0.2f + i / 80f,
                    green = 0.45f,
                    blue = 0.9f,
                    alpha = 0.55f,
                ),
                radius = 5f + (i % 4),
                center = Offset(x, y),
            )
        }
    }
}


@Composable
fun FrameJankLogger(tag: String) {
    LaunchedEffect(tag) {
        var last = 0L
        while (true) {
            val now = withFrameNanos { it }
            if (last != 0L) {
                val frameMs = (now - last) / 1_000_000f
                if (frameMs > 24f) {
                    println("$tag jank frame=${frameMs}ms")
                }
            }
            last = now
        }
    }
}

@Composable
fun RecomposeLogger(tag: String) {
    var count = remember { 0 }
    SideEffect {
        count += 1
        if (count % 20 == 0) {
            println("$tag recomposed $count times")
        }
    }
}