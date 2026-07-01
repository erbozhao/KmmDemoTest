package com.example.kmmdemotest.ksp

import androidx.compose.runtime.Composable

interface ISplash {
    /**
     * 闪屏优先级,数字越大优先级越高
     */
    val splashPriority: Int

    fun canShow(openType: Int): Boolean

    @Composable
    fun ShowSplash(openType: Int)
}