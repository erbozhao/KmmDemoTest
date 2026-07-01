package com.example.kmmdemotest.ksp

import androidx.compose.runtime.Composable

class TestSplash : ISplash {
    override val splashPriority: Int = 0

    override fun canShow(openType: Int): Boolean {
        return false
    }

    @Composable
    override fun ShowSplash(openType: Int) {

    }
}