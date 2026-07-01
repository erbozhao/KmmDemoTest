package com.example.kmmdemotest.kibo

import androidx.collection.LruCache
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

/**
 * @author onuszhao
 * @since 2025/10/14
 * @description
 */

private val cacheTextLayoutResults = LruCache<String, TextLayoutResult>(3000)

@Composable
fun KBCacheText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current
) {
    // val density = LocalDensity.current
    // val resolver = LocalFontFamilyResolver.current
    // val textColor = color.takeOrElse { style.color.takeOrElse { LocalContentColor.current } }
    // val textStyle = style.merge(
    //     color = textColor,
    //     fontSize = fontSize,
    //     fontWeight = fontWeight,
    //     textAlign = textAlign ?: TextAlign.Unspecified,
    //     lineHeight = lineHeight,
    //     fontFamily = fontFamily,
    //     textDecoration = textDecoration,
    //     fontStyle = fontStyle,
    //     letterSpacing = letterSpacing
    // )
    // val cacheKey = text
    // // val cacheKey = buildString {
    // //     append(text)
    // //     append("|$maxLines")
    // //     append("|$overflow")
    // //     append("|${textStyle.fontSize}")
    // //     append("|${textStyle.fontWeight?.weight}")
    // //     append("|${textStyle.fontFamily}")
    // //     append("|${textStyle.lineHeight}")
    // // }
    //
    // var textLayoutResult = cacheTextLayoutResults[cacheKey]
    // if (textLayoutResult != null) {
    //     Canvas(Modifier.width(textLayoutResult.size.width.toDp()).height(textLayoutResult.size.height.toDp())) {
    //         drawText(
    //             textLayoutResult = textLayoutResult,
    //         )
    //     }
    // } else {
    //     BoxWithConstraints(modifier = modifier) {
    //         val textMeasurer = TextMeasurer(resolver, density, LayoutDirection.Ltr)
    //         val maxWidthPx = with(density) { constraints.maxWidth }
    //         val constraints = Constraints(minWidth = 0, maxWidth = maxWidthPx, minHeight = 0)
    //         textLayoutResult = textMeasurer.measure(text = text, style = textStyle, overflow = overflow, maxLines = maxLines, constraints = constraints)
    //         // textLayoutResult = textMeasurer.measure(text = text, style = textStyle)
    //
    //         println("KBCacheText  cacheKey: $cacheKey")
    //         cacheTextLayoutResults.put(cacheKey, textLayoutResult)
    //
    //         Canvas(Modifier.width(textLayoutResult.size.width.toDp()).height(textLayoutResult.size.height.toDp())) {
    //             drawText(
    //                 textLayoutResult = textLayoutResult,
    //             )
    //         }
    //     }
    // }
}