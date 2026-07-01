package com.example.kmmdemotest.ext

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * CloudView limited 2025 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2025/4/21
 * @Time: 14:05
 * @version: V1.0
 */

fun Modifier.exposure(
    orientation: Orientation = Orientation.Horizontal,
    debounceTimeMillis: Long = 100L, // 默认防抖时间为 100 毫秒
    ignoreExposed: Boolean = false, // 是否忽略已经曝光的判断
    onExposure: () -> Unit
): Modifier = composed {
    // 使用 remember 创建一个稳定的状态变量，用于存储曝光状态
    val isExposed = remember { mutableStateOf(false) }
    // 使用 rememberCoroutineScope 创建一个 CoroutineScope，用于管理协程的生命周期
    val scope = rememberCoroutineScope()
    // 使用 remember 创建一个 Job 对象，用于取消之前的防抖任务
    val exposureJob = remember { mutableStateOf<Job?>(null) }

    this then onGloballyPositioned { layoutCoordinates: LayoutCoordinates ->
        if (layoutCoordinates.isAttached) {
            val bounds = layoutCoordinates.boundsInParent()
            val parentCoordinates = layoutCoordinates.parentLayoutCoordinates ?: return@onGloballyPositioned
            // 根据指定的方向进行曝光检测
            val isCurrentlyExposed = if (orientation == Orientation.Horizontal) {
                bounds.right - bounds.width / 2 < parentCoordinates.size.width
            } else {
                bounds.bottom - bounds.height / 2 < parentCoordinates.size.height
            }
            if (isCurrentlyExposed) {
                // 如果当前 Composable 处于曝光状态，则启动或重启防抖计时器
                exposureJob.value?.cancel() // 取消之前的任务
                exposureJob.value = scope.launch(Dispatchers.IO) {
                    delay(debounceTimeMillis) // 延迟指定的时间
                    if (ignoreExposed) {
                        onExposure() // 执行曝光回调
                    } else {
                        if (!isExposed.value) { // 仅在之前未曝光时执行回调
                            isExposed.value = true // 更新曝光状态
                            onExposure() // 执行曝光回调
                        }
                    }
                }
            } else {
                // 如果当前 Composable 不在曝光状态，则取消防抖计时器，并重置曝光状态
                // exposureJob.value?.cancel()
                // isExposed.value = false // 重置曝光状态
            }
        }
    }
}

fun Modifier.checkExposure(
    pageIndex: Int,
    state: LazyListState,
    onExposure: () -> Unit
): Modifier = composed {
    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleItemIndex }.distinctUntilChanged().collectLatest {
            if (it == pageIndex) {
                onExposure()
            }
        }
    }
    this
}
