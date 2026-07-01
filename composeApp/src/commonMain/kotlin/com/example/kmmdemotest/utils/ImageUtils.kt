package com.example.kmmdemotest.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.NativeCanvas

/**
 * @author onuszhao
 * @since 2025/10/16
 * @description
 */

expect object ImageUtils {
    fun convertToImageBitmap(byteArray: ByteArray): ImageBitmap?

    fun convertToImageBitmap(canvas: NativeCanvas): ImageBitmap?

    fun convertToByteArray(imageBitmap: ImageBitmap): ByteArray?

    fun saveByteArrayToFile(byteArray: ByteArray, directoryPath: String, fileName: String): Boolean

    fun convertSoftwareBitmap(imageBitmap: ImageBitmap): ImageBitmap
}