package com.example.kmmdemotest.ext

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.jvm.JvmInline
import kotlin.math.abs
import kotlin.math.absoluteValue

/**
 * @author onuszhao
 * @since 2025/10/9
 * @description
 */

@Composable
fun rememberCustomSnapFlingBehavior(
    lazyListState: LazyListState,
    snapPosition: SnapPosition = SnapPosition.Center
): FlingBehavior {
    val snappingLayout = remember(lazyListState) { customSnapLayoutInfoProvider(lazyListState, snapPosition) }
    return rememberSnapFlingBehavior(snappingLayout)
}

fun customSnapLayoutInfoProvider(lazyListState: LazyListState, snapPosition: SnapPosition = SnapPosition.Center): SnapLayoutInfoProvider = object : SnapLayoutInfoProvider {

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
        return 0f
        // return (decayOffset.absoluteValue - averageItemSize).coerceAtLeast(0.0f) * decayOffset.sign
    }

    override fun calculateSnapOffset(velocity: Float): Float {
        return 0f

        // var lowerBoundOffset = Float.NEGATIVE_INFINITY
        // var upperBoundOffset = Float.POSITIVE_INFINITY
        //
        // layoutInfo.visibleItemsInfo.fastForEach { item ->
        //     if ((item as? LazyLayoutMeasuredItem)?.nonScrollableItem == true) return@fastForEach
        //     val offset =
        //         calculateDistanceToDesiredSnapPosition(
        //             mainAxisViewPortSize = layoutInfo.singleAxisViewportSize,
        //             beforeContentPadding = layoutInfo.beforeContentPadding,
        //             afterContentPadding = layoutInfo.afterContentPadding,
        //             itemSize = item.size,
        //             itemOffset = item.offset,
        //             itemIndex = item.index,
        //             snapPosition = snapPosition,
        //             itemCount = layoutInfo.totalItemsCount,
        //         )
        //
        //     // Find item that is closest to the center
        //     if (offset <= 0 && offset > lowerBoundOffset) {
        //         lowerBoundOffset = offset
        //     }
        //
        //     // Find item that is closest to center, but after it
        //     if (offset >= 0 && offset < upperBoundOffset) {
        //         upperBoundOffset = offset
        //     }
        // }
        //
        // return calculateFinalOffset(
        //     with(lazyListState.density) { calculateFinalSnappingItem(velocity) },
        //     lowerBoundOffset,
        //     upperBoundOffset,
        // )
    }
}

internal val MinFlingVelocityDp = 400.dp
internal const val NoDistance = 0f
internal const val NoVelocity = 0f

internal fun calculateFinalOffset(
    snappingOffset: FinalSnappingItem,
    lowerBound: Float,
    upperBound: Float,
): Float {

    fun Float.isValidDistance(): Boolean {
        return this != Float.POSITIVE_INFINITY && this != Float.NEGATIVE_INFINITY
    }

    val finalDistance =
        when (snappingOffset) {
            FinalSnappingItem.ClosestItem -> {
                if (abs(upperBound) <= abs(lowerBound)) {
                    upperBound
                } else {
                    lowerBound
                }
            }
            FinalSnappingItem.NextItem -> upperBound
            FinalSnappingItem.PreviousItem -> lowerBound
            else -> NoDistance
        }

    return if (finalDistance.isValidDistance()) {
        finalDistance
    } else {
        NoDistance
    }
}

internal fun calculateDistanceToDesiredSnapPosition(
    mainAxisViewPortSize: Int,
    beforeContentPadding: Int,
    afterContentPadding: Int,
    itemSize: Int,
    itemOffset: Int,
    itemIndex: Int,
    snapPosition: SnapPosition,
    itemCount: Int
): Float {
    val desiredDistance = with(snapPosition) {
        position(
            mainAxisViewPortSize,
            itemSize,
            beforeContentPadding,
            afterContentPadding,
            itemIndex,
            itemCount
        )
    }.toFloat()

    return itemOffset - desiredDistance
}

internal val LazyListLayoutInfo.singleAxisViewportSize: Int
    get() = if (orientation == Orientation.Vertical) viewportSize.height else viewportSize.width

@JvmInline
internal value class FinalSnappingItem
internal constructor(@Suppress("unused") private val value: Int) {
    companion object {

        val ClosestItem: FinalSnappingItem = FinalSnappingItem(0)

        val NextItem: FinalSnappingItem = FinalSnappingItem(1)

        val PreviousItem: FinalSnappingItem = FinalSnappingItem(2)
    }
}

internal fun Density.calculateFinalSnappingItem(velocity: Float): FinalSnappingItem {
    return if (velocity.absoluteValue < MinFlingVelocityDp.toPx()) {
        FinalSnappingItem.ClosestItem
    } else {
        if (velocity > 0) FinalSnappingItem.NextItem else FinalSnappingItem.PreviousItem
    }
}

internal interface LazyLayoutMeasuredItem {
    val index: Int
    val key: Any
    val isVertical: Boolean
    val mainAxisSizeWithSpacings: Int
    val placeablesCount: Int
    var nonScrollableItem: Boolean
    val constraints: Constraints
    val lane: Int
    val span: Int

    fun getOffset(index: Int): IntOffset

    fun position(mainAxisOffset: Int, crossAxisOffset: Int, layoutWidth: Int, layoutHeight: Int)

    fun getParentData(index: Int): Any?
}