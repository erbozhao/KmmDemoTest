package com.example.kmmdemotest.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

actual object ImageUtils {

    actual fun convertToImageBitmap(byteArray: ByteArray): ImageBitmap? {
        val androidBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return androidBitmap.asImageBitmap()
    }

    actual fun convertToImageBitmap(canvas: Canvas): ImageBitmap? {
        return null

        // val width = canvas.size.width.toInt()
        // val height = canvas.size.height.toInt()
        //
        // // 创建一个Android Bitmap
        // val androidBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //
        // // 使用Canvas绘制到Bitmap上
        // val canvas = android.graphics.Canvas(androidBitmap)
        // this.drawIntoCanvas { nativeCanvas ->
        //     // 在nativeCanvas上绘制
        //     canvas.drawPicture(nativeCanvas.nativeCanvas)
        // }
        //
        // // 转换为Compose ImageBitmap
        // return androidBitmap.asImageBitmap()
    }

    actual fun convertToByteArray(imageBitmap: ImageBitmap): ByteArray? {
        val bitmap = imageBitmap.asAndroidBitmap()
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    actual fun saveByteArrayToFile(byteArray: ByteArray, directoryPath: String, fileName: String): Boolean {
        // 注意：在 Android 上，文件权限和路径（如 MediaStore 或应用内部存储）需要仔细处理
        try {
            val directory = File(directoryPath)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, fileName)
            FileOutputStream(file).use {
                it.write(byteArray)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    actual fun convertSoftwareBitmap(imageBitmap: ImageBitmap): ImageBitmap {
        // return imageBitmap.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, true).asImageBitmap()

        val androidBitmap = imageBitmap.asAndroidBitmap()
        if (androidBitmap.config == Bitmap.Config.HARDWARE) {
            val softwareBitmap = androidBitmap.copy(Bitmap.Config.ARGB_8888, true)
            return softwareBitmap.asImageBitmap()
        }
        return imageBitmap
    }
}