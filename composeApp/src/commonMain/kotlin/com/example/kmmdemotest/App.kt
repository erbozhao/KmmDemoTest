package com.example.kmmdemotest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmmdemotest.demo.FoldableAdapteDemo
import com.example.kmmdemotest.utils.LogUtils
import kotlinx.coroutines.isActive

@Composable
fun App() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        MaterialTheme {
            Box(modifier = Modifier.fillMaxSize()) {
                // DemoView()
                // ListViewDemo()
                // LargeMemoryView()
                // LagPageView()
                // LagPageView2()
                // InsertListViewDemo()
                // BookViewDemo()
                // BookViewDemo2()
                // BookViewDemo3()
                // BookViewDemo4()
                // AnimationView()
                // BookCanvasView()

                FoldableAdapteDemo()

                // FpsMonitorView()

            }
        }
    }
}

@Composable
fun FpsMonitorView() {
    val fpsList = remember { mutableStateListOf<Int>() }

    LaunchedEffect(Unit) {
        var frameCount = 0
        var lastTime = withFrameNanos { it }

        while (isActive) {
            withFrameNanos { nanos ->
                frameCount++
                val elapsed = nanos - lastTime
                if (elapsed >= 1_000_000_000L) {
                    fpsList.add(frameCount)
                    val avg = fpsList.average().toInt()
                    LogUtils.d("FPS") { "fps: $frameCount  history: $fpsList  avg: $avg" }
                    frameCount = 0
                    lastTime = nanos
                }
            }
        }
    }

    val avg = if (fpsList.isEmpty()) 0 else fpsList.average().toInt()
    val historyText = fpsList.joinToString(" | ")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 52.dp, end = 8.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Text(
            text = "History: $historyText\nAvg: $avg",
            color = Color.Green,
            fontSize = 14.sp,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
