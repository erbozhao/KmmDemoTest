package com.example.kmmdemotest

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.ComposeRuntimeFlags
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.uikit.ComposeUIViewControllerDelegate
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIStatusBarStyle

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class, ExperimentalFoundationApi::class)
fun MainViewController() = run{
    ComposeRuntimeFlags.isLinkBufferComposerEnabled = true //开启SlotTable，重组大约快10%
    ComposeFoundationFlags.isPausableCompositionInPrefetchEnabled = true   //开启Lazy预取分帧

    ComposeUIViewController(configure = {
        this.parallelRendering = true
        delegate = object : ComposeUIViewControllerDelegate {
            override val preferredStatusBarStyle: UIStatusBarStyle?
                get() = 0
        }
    }) { App() }
}