package com.example.kmmdemotest.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.unit.dp
import com.example.kmmdemotest.Greeting
import com.example.kmmdemotest.ext.toDp
import com.example.kmmdemotest.ext.toPx
import com.example.kmmdemotest.handler.CoroutineHandler
import com.example.kmmdemotest.handler.CoroutineRunnable
import com.example.kmmdemotest.handler.MessageHandler
import com.example.kmmdemotest.handler.MessageHandler2
import com.example.kmmdemotest.utils.LogUtils
import com.example.kmmdemotest.utils.TimeUtils
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeId

/**
 * @author onuszhao
 * @since 2025/10/27
 * @description
 */

@Composable
fun DemoView() {
    // 1. 创建 GraphicsLayer 实例
    val graphicsLayer = rememberGraphicsLayer()
    val coroutineScope = rememberCoroutineScope()

    // 用于存储捕获到的 ImageBitmap
    var capturedBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize()
            .drawWithContent {
                // 使用 record 捕获内容
                graphicsLayer.record {
                    // 绘制当前 Composable 的内容
                    this@drawWithContent.drawContent()
                }
                // 绘制 GraphicsLayer 的内容到屏幕上，确保它被渲染
                drawLayer(graphicsLayer)
            }
            .clickable {
                // 点击 Box 或其他触发事件时捕获 ImageBitmap
                coroutineScope.launch {
                    // 3. 将 GraphicsLayer 转换为 ImageBitmap
                    val bitmap = graphicsLayer.toImageBitmap()
                    capturedBitmap = bitmap
                    LogUtils.d("onuszhao", "bitmap=${bitmap}  capturedBitmap=$capturedBitmap  ${capturedBitmap?.getScopeId()}")
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TestAnim()
        TestMessageHandler()
        TestMessageHandler2()
        TestCoroutineHandler()
    }
}

@Composable
fun TestAnim() {
    var showContent by remember { mutableStateOf(false) }
    Button(onClick = { showContent = !showContent }) {
        Text("Click me!")
    }
    AnimatedVisibility(showContent) {
        val greeting = remember { Greeting().greet() }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Image(painterResource(Res.drawable.compose_multiplatform), null)
            Text("Compose: $greeting")

            val needReadingProgress by rememberUpdatedState(true)
            val progress = remember { Animatable(0.0f) }
            val totalDurationMillis = 30000
            val updateIntervalMillis = 1000
            val progressStep = (updateIntervalMillis * 1.0f / totalDurationMillis) * (1.0f - 0.18f)
            LaunchedEffect(needReadingProgress) {
                if (needReadingProgress) {
                    progress.snapTo(0.0f)
                    progress.animateTo(
                        targetValue = 1.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 30000, easing = LinearEasing)
                        )
                    )
                    while (needReadingProgress) {
                        var newProgress = progress.value + progressStep
                        if (newProgress > 1.0f) {
                            newProgress = 0.18f
                        }
                        progress.snapTo(newProgress)
                        delay(updateIntervalMillis.toLong())
                    }
                } else {
                    progress.stop()
                    progress.snapTo(0.0f)
                }
            }
            val bgWidth = (50.dp.toPx() * progress.value).toDp()
            Box(modifier = Modifier.width(bgWidth).height(20.dp).background(Color.Red.copy(alpha = 0.3f)))
        }
    }
}

@Composable
fun TestMessageHandler() {

    val handler = remember { MessageHandler() }

    var needRun by remember { mutableStateOf(false) }

    LaunchedEffect(needRun) {
        if (needRun) {
            handler.post {
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test1")
            }

            handler.postDelayed({
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test2")
            }, 3000)

            handler.post {
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test3")
            }

            handler.postDelayed({
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test4")
            }, 10000)

            handler.postDelayed({
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test5")
            }, 1000)

            handler.post {
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test6")
            }

            needRun = false
        }
    }
    Button(onClick = { needRun = true }) {
        Text("Test MessageHandler")
    }
}

@Composable
fun TestMessageHandler2() {

    val handler = remember { MessageHandler2() }

    var needRun by remember { mutableStateOf(false) }

    LaunchedEffect(needRun) {
        if (needRun) {
            handler.post(
                object : CoroutineRunnable {
                    override suspend fun run() {
                        println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test1")
                    }
                }
            )

            handler.postDelayed(object : CoroutineRunnable {
                override suspend fun run() {
                    println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test2")
                }
            }, 3000)

            handler.post(object : CoroutineRunnable {
                override suspend fun run() {
                    println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test3")
                }
            })

            val task = object : CoroutineRunnable {
                override suspend fun run() {
                    println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test4")
                }
            }
            println("testHandler  ${task.getScopeId()}")
            handler.postDelayed(task, 10000)

            handler.postDelayed(object : CoroutineRunnable {
                override suspend fun run() {
                    println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test5")
                }
            }, 1000)

            handler.post(
                object : CoroutineRunnable {
                    override suspend fun run() {
                        println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test6")
                    }
                }
            )
            handler.remove(task)

            needRun = false
        }
    }
    Button(onClick = { needRun = true }) {
        Text("Test MessageHandler2")
    }
}

@Composable
fun TestCoroutineHandler() {

    val handler = remember { CoroutineHandler() }

    var needRun by remember { mutableStateOf(false) }

    LaunchedEffect(needRun) {
        if (needRun) {
            handler.post {
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test1")
            }

            handler.postDelayed({
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test2")
            }, 3000)

            handler.post {
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test3")
            }

            handler.postDelayed({
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test4")
            }, 10000)

            handler.postDelayed({
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test5")
            }, 1000)

            handler.post {
                println("${TimeUtils.formatDate(getTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}  start test6")
            }

            needRun = false
        }
    }
    Button(onClick = { needRun = true }) {
        Text("Test CoroutineHandler")
    }
}