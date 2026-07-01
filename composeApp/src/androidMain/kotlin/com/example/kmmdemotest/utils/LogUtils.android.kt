package com.example.kmmdemotest.utils

import android.util.Log

internal actual fun print(level: LogUtils.LogLevel, tag: String, message: String) {
    when (level) {
        LogUtils.LogLevel.INFO -> Log.i(tag, message)
        LogUtils.LogLevel.DEBUG -> Log.d(tag, message)
        LogUtils.LogLevel.WARNING -> Log.w(tag, message)
        LogUtils.LogLevel.ERROR -> Log.e(tag, message)
        LogUtils.LogLevel.NONE -> Log.i(tag, message)
    }
}