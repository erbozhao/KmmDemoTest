package com.example.kmmdemotest.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * @author onuszhao
 * @since 2025/10/30
 * @description
 */

@Composable
fun Int.toDp(): Dp {
    val pixelValue = this
    val density = LocalDensity.current // 获取当前的 Density 对象
    val dpValue: Dp = with(density) { pixelValue.toDp() } // 使用 toDp() 扩展函数进行转换
    return dpValue
}

@Composable
fun Float.toDp(): Dp {
    val pixelValue = this
    val density = LocalDensity.current // 获取当前的 Density 对象
    val dpValue: Dp = with(density) { pixelValue.toDp() } // 使用 toDp() 扩展函数进行转换
    return dpValue
}

@Composable
fun Dp.toPx(): Float {
    val dpValue = this
    val density = LocalDensity.current
    val pixelValue: Float = with(density) { dpValue.toPx() }
    return pixelValue
}