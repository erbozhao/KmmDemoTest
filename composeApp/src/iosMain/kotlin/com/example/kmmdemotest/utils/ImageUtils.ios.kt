package com.example.kmmdemotest.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.NativeCanvas
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual object ImageUtils {
    actual fun convertToImageBitmap(byteArray: ByteArray): ImageBitmap? {
        return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
    }

    actual fun convertToImageBitmap(canvas: NativeCanvas): ImageBitmap? {
        return null
        // val width = this.size.width.toInt()
        // val height = this.size.height.toInt()
        //
        // // 创建一个Core Graphics上下文
        // val colorSpace = CGColorSpaceCreateDeviceRGB()
        // val bitmapInfo = CGBitmapInfo.PremultipliedLast.rawValue
        //
        // val context = CGContext(
        //     bitmapData = null,
        //     width = width,
        //     height = height,
        //     bitsPerComponent = 8,
        //     bytesPerRow = 0,
        //     space = colorSpace,
        //     bitmapInfo = bitmapInfo
        // )
        //
        // // 使用Canvas绘制到context上
        // context?.let {
        //     this.drawIntoCanvas { nativeCanvas ->
        //         it.drawPicture(nativeCanvas.nativeCanvas)
        //     }
        // }
        //
        // // 获取UIImage并转换为ImageBitmap
        // val cgImage = it.makeImage() ?: return ImageBitmap(0, 0)
        // val uiImage = UIImage(cgImage: cgImage)
        // return uiImage.asImageBitmap()
    }

    actual fun convertToByteArray(imageBitmap: ImageBitmap): ByteArray? {
        val skiaBitmap = imageBitmap.asSkiaBitmap()
        return Image.makeFromBitmap(skiaBitmap).encodeToData(EncodedImageFormat.PNG, 100)?.bytes
    }

    actual fun saveByteArrayToFile(byteArray: ByteArray, directoryPath: String, fileName: String): Boolean {
        // try {
        //     val file = java.io.File(directoryPath, fileName)
        //     file.writeBytes(byteArray) // Kotlin 扩展函数
        //     return true
        // } catch (e: Exception) {
        //     e.printStackTrace()
        //     return false
        // }
        return false
    }

    actual fun convertSoftwareBitmap(imageBitmap: ImageBitmap): ImageBitmap {
        // val skiaBitmap = imageBitmap.asSkiaBitmap()
        // return Image.makeFromBitmap(skiaBitmap).toComposeImageBitmap()

        return imageBitmap
    }
}