package com.example.kmmdemotest.utils

import platform.Foundation.NSLog

internal actual fun print(level: LogUtils.LogLevel, tag: String, message: String) {
    val logMessage = when (level) {
        LogUtils.LogLevel.DEBUG -> "DEBUG[$tag]: $message"
        LogUtils.LogLevel.INFO -> "INFO[$tag]: $message"
        LogUtils.LogLevel.WARNING -> "WARN[$tag]: $message"
        LogUtils.LogLevel.ERROR -> "ERROR[$tag]: $message"
        LogUtils.LogLevel.NONE -> "NONE[$tag]: $message"
    }
    try {
        NSLog(logMessage)
    } catch (e: Throwable) {
        println("Failed to save file: ${e.message}")
    }
}