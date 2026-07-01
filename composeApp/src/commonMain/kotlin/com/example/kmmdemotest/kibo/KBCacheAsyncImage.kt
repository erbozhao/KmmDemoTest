package com.example.kmmdemotest.kibo

/**
 * @author onuszhao
 * @since 2025/10/14
 * @description
 */

// private val cacheImagePainters = LruCache<String, Painter>(2000)
//
// @Composable
// fun KBCacheAsyncImage(
//     model: String?,
//     contentDescription: String?,
//     modifier: Modifier = Modifier,
//     contentScale: ContentScale = ContentScale.Crop,
//     placeholder: Painter = ColorPainter(Color.Transparent),
//     error: Painter = ColorPainter(Color.Transparent),
// ) {
//     LogUtils.d("onuszhao", "model=$model")
//     if (model.isNullOrBlank()) {
//         drawPainter(placeholder, modifier, contentScale)
//         // Image(
//         //     painter = placeholder,
//         //     contentDescription = contentDescription,
//         //     modifier = modifier,
//         //     contentScale = contentScale,
//         // )
//         return
//     }
//
//     val cachedPainter = cacheImagePainters[model]
//     if (cachedPainter != null) {
//         LogUtils.d("onuszhao", "drawPainter  cachedPainter=$cachedPainter model=$model")
//         drawPainter(cachedPainter, modifier, contentScale)
//         // Image(
//         //     painter = cachedPainter,
//         //     contentDescription = contentDescription,
//         //     modifier = modifier,
//         //     contentScale = contentScale,
//         // )
//         return
//     }
//
//     val asyncPainter = rememberAsyncImagePainter(
//         model = model,
//         contentScale = contentScale,
//         placeholder = placeholder,
//         error = error,
//         onSuccess = { state ->
//             cacheImagePainters.put(model, state.painter)
//         }
//     )
//
//     LogUtils.d("onuszhao", "drawPainter  asyncPainter=$asyncPainter model=$model")
//     drawPainter(asyncPainter, modifier, contentScale)
//     // Image(
//     //     painter = asyncPainter,
//     //     contentDescription = contentDescription,
//     //     modifier = modifier,
//     //     contentScale = contentScale,
//     // )
// }
//
// @Composable
// fun drawPainter(
//     painter: Painter,
//     modifier: Modifier = Modifier,
//     contentScale: ContentScale = ContentScale.Crop,
// ) {
//     Canvas(modifier = modifier) {
//         val intrinsicSize = painter.intrinsicSize.takeOrElse { size }
//
//         val scaleFactor = contentScale.computeScaleFactor(srcSize = intrinsicSize, dstSize = size)
//         val scaledWidth = intrinsicSize.width * scaleFactor.scaleX
//         val scaledHeight = intrinsicSize.height * scaleFactor.scaleY
//
//         val offset = Alignment.Center.align(
//             size = Size(scaledWidth, scaledHeight).roundToIntSize(), // 缩放后的尺寸
//             space = size.roundToIntSize(),     // Canvas 的尺寸
//             layoutDirection = layoutDirection
//         )
//
//         // 应用 Canvas裁剪（如果 ContentScale 需要裁剪，例如 ContentScale.Crop）
//         clipRect(
//             left = 0f, top = 0f, right = size.width, bottom = size.height,
//             clipOp = ClipOp.Intersect
//         ) {
//             with(painter) {
//                 val scaleX = scaledWidth / intrinsicSize.width
//                 val scaleY = scaledHeight / intrinsicSize.height
//                 translate(left = offset.x.toFloat(), top = offset.y.toFloat()) {
//                     scale(scaleX = scaleX, scaleY = scaleY, pivot = Offset.Zero) {
//                         draw(size = intrinsicSize, alpha = 1.0f)
//                     }
//                 }
//             }
//         }
//     }
// }
