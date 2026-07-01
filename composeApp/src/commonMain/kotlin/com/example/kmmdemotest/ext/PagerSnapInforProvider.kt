package com.example.kmmdemotest.ext

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * CloudView limited 2025 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2025/3/25
 * @Time: 14:25
 * @version: V1.0
 */
fun mySnapLayoutInfoProvider(
    lazyListState: LazyListState,
    snapPosition: SnapPosition = SnapPosition.Center
): SnapLayoutInfoProvider = object : SnapLayoutInfoProvider {

    private val layoutInfo: LazyListLayoutInfo
        get() = lazyListState.layoutInfo

    private val averageItemSize: Int
        get() {
            val layoutInfo = layoutInfo
            return if (layoutInfo.visibleItemsInfo.isEmpty()) {
                0
            } else {
                val numberOfItems = layoutInfo.visibleItemsInfo.size
                layoutInfo.visibleItemsInfo.sumOf {
                    it.size
                } / numberOfItems
            }
        }

    override fun calculateApproachOffset(velocity: Float, decayOffset: Float): Float {
        //直接结束 fling 直接进入 snapoffset
        return 0f
    }

    // 此方案向右翻页会留边
    // override fun calculateSnapOffset(velocity: Float): Float {
    //     var lowerBoundOffset = Float.NEGATIVE_INFINITY
    //     var upperBoundOffset = Float.POSITIVE_INFINITY
    //
    //     layoutInfo.visibleItemsInfo.fastForEach { item ->
    //         val offset =
    //             calculateDistanceToDesiredSnapPosition(
    //                 mainAxisViewPortSize = layoutInfo.singleAxisViewportSize,
    //                 beforeContentPadding = layoutInfo.beforeContentPadding,
    //                 afterContentPadding = layoutInfo.afterContentPadding,
    //                 itemSize = item.size,
    //                 itemOffset = item.offset,
    //                 itemIndex = item.index,
    //                 snapPosition = snapPosition,
    //                 itemCount = layoutInfo.totalItemsCount
    //             )
    //
    //         // Find item that is closest to the center
    //         if (offset <= 0 && offset > lowerBoundOffset) {
    //             lowerBoundOffset = offset
    //         }
    //
    //         // Find item that is closest to center, but after it
    //         if (offset >= 0 && offset < upperBoundOffset) {
    //             upperBoundOffset = offset
    //         }
    //     }
    //
    //     return if (velocity == 0f) {
    //         // 若速度为0，根据滑动方向来判断
    //         if (lazyListState.lastScrolledForward) { //向左翻页(向前滑)
    //             upperBoundOffset
    //         } else if (lazyListState.lastScrolledBackward) { //向右翻页(向后滑)
    //             lowerBoundOffset
    //         } else {
    //             0f
    //         }
    //     } else if (velocity > 0)
    //         upperBoundOffset
    //     else {
    //         lowerBoundOffset
    //     }
    //     // return calculateFinalOffset(
    //     //     with(lazyListState.density) { calculateFinalSnappingItem(velocity) },
    //     //     lowerBoundOffset,
    //     //     upperBoundOffset
    //     // )
    // }

    override fun calculateSnapOffset(velocity: Float): Float {
        val layoutInfo = lazyListState.layoutInfo

        // 获取当前可见的第一个item的信息, 若没有可见item，或者已经完全对齐，则不需要移动
        val firstVisibleItem = layoutInfo.visibleItemsInfo.firstOrNull()
        if (firstVisibleItem == null || firstVisibleItem.offset == 0) {
            return 0f
        }

        val currentOffset = firstVisibleItem.offset.toFloat() // 已偏移位置，通常为负数或零
        val itemWidth = layoutInfo.viewportSize.width.toFloat() // 视口宽度，通常用于代表item宽度

        // 计算吸附到目标位置的距离
        val snapOffset = if (velocity > 0) { //向左翻页
            // 剩余需要偏移量 = item 宽度 + 已偏移位置 (currentOffset 为负数，所以是相减)
            itemWidth + currentOffset
        } else if (velocity < 0) { // 向右翻页
            // 距离 first item 的距离就是需要偏移量 (currentOffset 此时为负数)
            currentOffset
        } else { // 若速度为0，根据滑动方向及阈值来判断
            // val halfItemWidth = itemWidth / 2f

            //若滑动距离过半，则翻页; 否则回弹
            if (lazyListState.lastScrolledForward) { //向左翻页(向前滑)
                itemWidth + currentOffset
                // if (currentOffset < -halfItemWidth) {
                //     itemWidth + currentOffset
                // } else {
                //     currentOffset
                // }
            } else if (lazyListState.lastScrolledBackward) { //向右翻页(向后滑)
                currentOffset
                // if (currentOffset > -halfItemWidth) {
                //     currentOffset
                // } else {
                //     itemWidth + currentOffset
                // }
            } else {
                0f
            }
        }
        return snapOffset
    }
}

/**
 * Create and remember a FlingBehavior for decayed snapping in Lazy Lists. This will snap
 * the item according to [snapPosition].
 *
 * @param lazyListState The [LazyListState] from the LazyList where this [FlingBehavior] will
 * be used.
 * @param snapPosition The desired positioning of the snapped item within the main layout.
 * This position should be considered with regards to the start edge of the item and the placement
 * within the viewport.
 */
@Composable
fun rememberReadViewSnapFlingBehavior(
    lazyListState: LazyListState,
    snapPosition: SnapPosition = SnapPosition.Center
): FlingBehavior {
    val snappingLayout =
        remember(lazyListState) { mySnapLayoutInfoProvider(lazyListState, snapPosition) }
    return rememberSnapFlingBehavior(snappingLayout)
}
