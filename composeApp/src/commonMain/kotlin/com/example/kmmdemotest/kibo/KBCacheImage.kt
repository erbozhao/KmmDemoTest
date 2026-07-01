package com.example.kmmdemotest.kibo

import androidx.collection.LruCache
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.takeOrElse
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.roundToIntSize
import org.jetbrains.compose.resources.DrawableResource

/**
 * @author onuszhao
 * @since 2025/10/15
 * @description
 */

private val cacheImagePainters = LruCache<String, Painter>(2000)

@Composable
fun KBCacheImage(
    resource: DrawableResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
) {

    // var imagePainter = cacheImagePainters[resource.getScopeId()]
    // if (imagePainter == null) {
    //     imagePainter = painterResource(resource)
    //     cacheImagePainters.put(resource.getScopeId(), imagePainter)
    // }
    // Image(
    //     painter = imagePainter,
    //     contentDescription = contentDescription,
    //     modifier = modifier,
    //     alignment = alignment,
    //     contentScale = contentScale,
    //     alpha = alpha,
    //     colorFilter = colorFilter
    // )

    // drawPainter(painter, modifier, contentScale)
}

@Composable
fun drawPainter(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onBitmapReady: (ImageBitmap) -> Unit
) {
    remember(painter) { ImageBitmap(width = 1, height = 1) }

    Canvas(modifier = modifier) {
        val intrinsicSize = painter.intrinsicSize.takeOrElse { size }

        val scaleFactor = contentScale.computeScaleFactor(srcSize = intrinsicSize, dstSize = size)
        val scaledWidth = intrinsicSize.width * scaleFactor.scaleX
        val scaledHeight = intrinsicSize.height * scaleFactor.scaleY

        val offset = Alignment.Center.align(
            size = Size(scaledWidth, scaledHeight).roundToIntSize(), // 缩放后的尺寸
            space = size.roundToIntSize(),     // Canvas 的尺寸
            layoutDirection = layoutDirection
        )

        // 应用 Canvas裁剪（如果 ContentScale 需要裁剪，例如 ContentScale.Crop）
        clipRect(
            left = 0f, top = 0f, right = size.width, bottom = size.height,
            clipOp = ClipOp.Intersect
        ) {
            with(painter) {
                val scaleX = scaledWidth / intrinsicSize.width
                val scaleY = scaledHeight / intrinsicSize.height
                translate(left = offset.x.toFloat(), top = offset.y.toFloat()) {
                    scale(scaleX = scaleX, scaleY = scaleY, pivot = Offset.Zero) {
                        draw(size = intrinsicSize, alpha = 1.0f)
                    }
                }
            }
        }
        onBitmapReady(ImageBitmap(size.width.toInt(), size.height.toInt()))
    }
}
