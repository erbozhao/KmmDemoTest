package com.example.kmmdemotest.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.example.kmmdemotest.utils.ImageUtils
import com.example.kmmdemotest.utils.LogUtils
import kmmdemotest.composeapp.generated.resources.Res
import kmmdemotest.composeapp.generated.resources.img_checken
import kmmdemotest.composeapp.generated.resources.img_mountain
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.math.tan

/**
 * CloudView limited 2025 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2025/10/29
 * @Time: 20:32
 * @version: V1.0
 */
@Composable
fun BookViewDemo() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        BookPager {
            Image(
                modifier = Modifier.fillMaxWidth(),
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
                painter = painterResource(Res.drawable.img_checken),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                text = "\t\t我为什么要关心她？"
                    + "\n\t\t你曾经说过，此生只爱她一个人的？因此你一直单身，对吧！"
                    + "\n\t\t梁医生，你忘了？"
                    + "\n\t\t我不是医生，我只是个打工仔，我也没有忘记，但是那份爱已经不会再有了"
                    + "\n\t\t嗨，她可是主动让我找你哦！"
                    + "\n\t\t听说，她小孩生病了！——张铭生说到。"
                    + "\n\t\t她真会找时间，她永远会在最困难的时候找我，永远会在没有困难的时候离我而去。"
                    + "\n\t\t事实或许相反，她离开你时已经是迫不得已，张铭生调高嗓门说到。"
                    + "\n\t\t"
            )
        }

        BookPager {
            Image(
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.Center,
                painter = painterResource(Res.drawable.img_mountain),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                text = "\t\t他清楚的知道，这个实验成功的概率是多么的低，他望着窗台的透进来的晨光，内心无比的焦灼，这才是早上九点种，但他仿佛看到了落日的余晖。"
                    + "\n\t\t病床的男人抽搐不停，他已经没有多长时间了，长期的抽搐，导致他无法入眠，如果这种状态再延续下去，走向人生的重点已成必然。"
                    + "\n\t\t梁雨，你有什么遗言么？"
                    + "\n\t\t他从窗台方向转向过来，看见他的初中老同学张铭生。"
                    + "\n\t\t我能有什么遗言，孤家寡人而已!"
                    + "\n\t\t嗯～啊？不想给张桐说几句么？听说她离婚了"
                    + "\n\t\t她说有很多话要对你说"
            )
        }

        // val listState = rememberLazyListState()
        // LazyRow(
        //     modifier = Modifier.fillMaxSize(),
        //     state = listState,
        //     flingBehavior = rememberReadViewSnapFlingBehavior(listState),
        //     overscrollEffect = NoOpOverscrollEffect(),
        //     userScrollEnabled = false,
        // ) {
        //     itemsIndexed(pageList) { index, item ->
        //         BookPager(modifier = Modifier.fillParentMaxSize(), content = {
        //             Image(
        //                 modifier = Modifier.fillMaxWidth(),
        //                 alignment = Alignment.Center,
        //                 contentScale = ContentScale.FillWidth,
        //                 painter = painterResource(Res.drawable.img_checken),
        //                 contentDescription = ""
        //             )
        //             Text(
        //                 modifier = Modifier
        //                     .fillMaxWidth()
        //                     .padding(5.dp),
        //                 text = "\t\t我为什么要关心她？"
        //                     + "\n\t\t你曾经说过，此生只爱她一个人的？因此你一直单身，对吧！"
        //                     + "\n\t\t梁医生，你忘了？"
        //                     + "\n\t\t我不是医生，我只是个打工仔，我也没有忘记，但是那份爱已经不会再有了"
        //                     + "\n\t\t嗨，她可是主动让我找你哦！"
        //                     + "\n\t\t听说，她小孩生病了！——张铭生说到。"
        //                     + "\n\t\t她真会找时间，她永远会在最困难的时候找我，永远会在没有困难的时候离我而去。"
        //                     + "\n\t\t事实或许相反，她离开你时已经是迫不得已，张铭生调高嗓门说到。"
        //                     + "\n\t\t"
        //             )
        //         })
        //
        //         BookPager(modifier = Modifier.fillParentMaxSize(), content = {
        //             Image(
        //                 modifier = Modifier.fillMaxWidth(),
        //                 contentScale = ContentScale.FillWidth,
        //                 alignment = Alignment.Center,
        //                 painter = painterResource(Res.drawable.img_mountain),
        //                 contentDescription = ""
        //             )
        //             Text(
        //                 modifier = Modifier
        //                     .fillMaxWidth()
        //                     .padding(15.dp),
        //                 text = "\t\t他清楚的知道，这个实验成功的概率是多么的低，他望着窗台的透进来的晨光，内心无比的焦灼，这才是早上九点种，但他仿佛看到了落日的余晖。"
        //                     + "\n\t\t病床的男人抽搐不停，他已经没有多长时间了，长期的抽搐，导致他无法入眠，如果这种状态再延续下去，走向人生的重点已成必然。"
        //                     + "\n\t\t梁雨，你有什么遗言么？"
        //                     + "\n\t\t他从窗台方向转向过来，看见他的初中老同学张铭生。"
        //                     + "\n\t\t我能有什么遗言，孤家寡人而已!"
        //                     + "\n\t\t嗯～啊？不想给张桐说几句么？听说她离婚了"
        //                     + "\n\t\t她说有很多话要对你说"
        //             )
        //         })
        //     }
        // }

    }
}

@Composable
private fun BookPager(modifier: Modifier = Modifier.fillMaxSize(), page: Page = Page(), content: @Composable ColumnScope.() -> Unit) {
    var pointerOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var dragState by remember {
        mutableIntStateOf(Page.STATE_IDLE)
    }
    val layer = rememberGraphicsLayer()
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .onSizeChanged {
                pointerOffset = Offset(it.width.toFloat(), it.height.toFloat())
            }
            .pointerInput("DraggerInput") {
                detectDragGestures(
                    onDragStart = { it ->
                        LogUtils.d("onuszhao", "onDragStart width=${size.width}  height=${size.height}  touch=(${it.x},${it})")

                        size.height / 3
                        val offsetLeft = 150.dp.toPx()
                        val offsetTop = 150.dp.toPx()

                        dragState = if (Rect(
                                size.width - offsetLeft,
                                size.height - 100.dp.toPx(),
                                size.width.toFloat(),
                                size.height.toFloat()
                            ).contains(it)
                        ) {
                            Page.STATE_DRAGING_BOTTOM
                        } else if (Rect(size.width - offsetLeft, 0F, size.width.toFloat(), 100.dp.toPx()).contains(it)) {
                            Page.STATE_DRAGING_TOP
                        } else if (Rect(
                                size.width - offsetLeft - 20.dp.toPx(),
                                offsetTop,
                                size.width - 20.dp.toPx(),
                                size.height - offsetTop
                            ).contains(it)
                        ) {
                            Page.STATE_DRAGING_MIDDLE
                        } else {
                            Page.STATE_DRAGING_EXCEEDE
                        }

                    },
                    onDragEnd = {
                        LogUtils.d("onuszhao", "onDragEnd")
                        dragState = Page.STATE_IDLE
                        pointerOffset = Offset(size.width.toFloat(), size.height.toFloat())
                    }
                ) { change, dragAmount ->
                    LogUtils.d(
                        "onuszhao",
                        "onDrag ${change.id}:(${change.position.x} ${change.position.y})  dragAmount:(${dragAmount.x} ${dragAmount.y})"
                    )
                    if (dragState == Page.STATE_DRAGING_BOTTOM || dragState == Page.STATE_DRAGING_MIDDLE || dragState == Page.STATE_DRAGING_TOP) {
                        pointerOffset = change.position
                    }
                }
            }
            .drawWithContent {
                // LogUtils.d("onuszhao", "drawWithContent dragState=$dragState  snapshot=${page.snapshot}  image=${page.imageBitmap}  width=${layer.size.width} width=${size.width}")

                if (dragState == Page.STATE_DRAGING_TOP) {
                    drawTopRightToLeft(this, pointerOffset, page)
                } else if (dragState == Page.STATE_DRAGING_BOTTOM) {
                    drawBottomRightToLeft(this, pointerOffset, page)
                } else if (dragState == Page.STATE_DRAGING_MIDDLE) {
                    drawMiddleDragState(this, pointerOffset, page)
                } else {
                    // Idle state - 在这里处理图像捕获
                    if (page.imageBitmap == null && page.snapshot && layer.size.width > 0) {
                        scope.launch {
                            kotlin.runCatching {
                                snapShotIfNeed(page, layer, size.width.toInt())
                            }.onFailure {
                                LogUtils.e("onuszhao", it.stackTraceToString())
                            }
                        }
                    }
                    drawIdleState(this, page, layer)
                }
            },
        content = content
    )
}

private suspend fun snapShotIfNeed(page: Page, layer: GraphicsLayer, canvasWidth: Int) {
    if (page.snapshot) {
        //如果不想StackOverflow的话，立即置为false，否则就做倒霉蛋吧
        page.snapshot = false

        // val imageBitmap = layer.toImageBitmap()
        val imageBitmap = ImageUtils.convertSoftwareBitmap(layer.toImageBitmap())

        val snapImageBitmap = ImageBitmap(imageBitmap.width, imageBitmap.height, ImageBitmapConfig.Argb8888)
        // val snapImageBitmap = ImageUtils.copyToNewImageBitmap(imageBitmap)!!
        val snapshotCanvas = Canvas(snapImageBitmap)
        val frontColor = page.frontColor
        page.frontColor = Color.Transparent

        snapshotCanvas.save()

        //翻转图像
        snapshotCanvas.scale(-1f, 1f) // 水平翻转
        snapshotCanvas.translate(-snapImageBitmap.width.toFloat(), 0f) // 因Canvas翻转，故此使用负值平移

        snapshotCanvas.drawImage(image = imageBitmap, topLeftOffset = Offset.Zero, paint = Paint())
        // snapshotCanvas.drawImageRect(
        //     image = imageBitmap,
        //     srcOffset = IntOffset.Zero,
        //     srcSize = IntSize(imageBitmap.width, imageBitmap.height),
        //     dstOffset = IntOffset.Zero,
        //     dstSize = IntSize(imageBitmap.width, imageBitmap.height),
        //     paint = Paint()
        // )

        snapshotCanvas.restore()

        // Log.d(TAG, "performDrawMethod = $imageBitmap")
        page.imageBitmap = snapImageBitmap
        page.frontColor = frontColor
    }
}

private fun ContentDrawScope.drawIdleState(
    canvas: ContentDrawScope,
    page: Page,
    layer: GraphicsLayer
) {
    layer.record {
        canvas.drawRect(page.frontColor)
        canvas.drawContent()
    }
    drawLayer(layer)
}

private fun drawIdleState(
    canvas: ContentDrawScope,
    page: Page
) {

    // if (page.snapshot) {
    //     //如果不想StackOverflow的话，立即置为false，否则就做倒霉蛋吧
    //     page.snapshot = false
    //
    //
    //     val LayoutNodeDrawScopeKlass =
    //         Class.forName("androidx.compose.ui.node.LayoutNodeDrawScope")
    //     if (LayoutNodeDrawScopeKlass.isInstance(canvas)) {
    //         val imageBitmap = ImageBitmap(canvas.size.width.toInt(), canvas.size.height.toInt())
    //         val drawNodeField = LayoutNodeDrawScopeKlass.getDeclaredField("drawNode")
    //         drawNodeField.isAccessible = true
    //         val drawModifierNode = drawNodeField.get(canvas) as DrawModifierNode
    //         val performDrawMethod =
    //             LayoutNodeDrawScopeKlass.getDeclaredMethod(
    //                 "performDraw",
    //                 DrawModifierNode::class.java,
    //                 Canvas::class.java
    //             )
    //         performDrawMethod.isAccessible = true
    //         val snapshotCanvas = Canvas(imageBitmap)
    //         val frontColor = page.frontColor
    //         page.frontColor = Color.Transparent
    //
    //         snapshotCanvas.save()
    //
    //         //翻转图像
    //         val matrix = Matrix()
    //         matrix[0,0] = -1f;
    //         matrix[3,0] = canvas.size.width
    //         snapshotCanvas.concat(matrix)
    //
    //         performDrawMethod.invoke(canvas, drawModifierNode, snapshotCanvas)
    //
    //         snapshotCanvas.restore()
    //         Log.d(TAG, "performDrawMethod = $imageBitmap")
    //         page.imageBitmap = imageBitmap
    //         page.frontColor = frontColor
    //     }
    // }

    canvas.drawRect(page.frontColor)
    canvas.drawContent()
}

private fun drawMiddleDragState(
    canvas: ContentDrawScope,
    pointerOffset: Offset,
    page: Page
) {

    val size = canvas.size
    val foldPath = page.foldPath
    val pageOutline = page.pageOutline
    val blankOutline = page.blankOutline
    val clipPath = page.clipPath

    canvas.translate(size.width, 0F) {
        val pointerPoint = Offset(
            pointerOffset.x - size.width,
            pointerOffset.y - 0
        )

        val verticalPoint = Offset(pointerPoint.x, 0F)
        val halfVerticalPoint = Offset(pointerPoint.x / 2F, 0F)

        foldPath.reset()
        foldPath.moveTo(verticalPoint.x, verticalPoint.y)
        foldPath.lineTo(halfVerticalPoint.x, halfVerticalPoint.y)
        foldPath.lineTo(halfVerticalPoint.x, size.height)
        foldPath.lineTo(verticalPoint.x, size.height)
        foldPath.close()


        pageOutline.reset()
        pageOutline.moveTo(-size.width, 0F)
        pageOutline.lineTo(-size.width, size.height)
        pageOutline.lineTo(0F, size.height)
        pageOutline.lineTo(0F, 0F)
        pageOutline.close()

        blankOutline.reset()
        blankOutline.moveTo(0F, 0F)
        blankOutline.lineTo(halfVerticalPoint.x, halfVerticalPoint.y)
        blankOutline.lineTo(halfVerticalPoint.x, size.height)
        blankOutline.lineTo(0F, size.height)
        blankOutline.close()

        clipPath.reset()
        clipPath.op(pageOutline, blankOutline, PathOperation.Difference)

        canvas.withTransform({
            clipPath(clipPath)
            translate(-size.width, 0F)
        }) {
            canvas.drawRect(page.frontColor)
            canvas.drawContent()
        }

        clipPath(foldPath) {
            canvas.drawRect(page.backColor, topLeft = Offset(verticalPoint.x, 0f), size = Size(size.width, size.height))
            page.imageBitmap?.let {
                drawImage(it, Offset(verticalPoint.x, 0f))
            }
        }

        canvas.drawLine(start = Offset(size.width, pointerPoint.y), end = pointerPoint, color = Color.Red)
        canvas.drawLine(start = halfVerticalPoint, end = Offset(halfVerticalPoint.x, size.height), color = Color.Blue)
        canvas.drawLine(start = verticalPoint, end = Offset(verticalPoint.x, size.height), color = Color.Blue)

    }
}

private fun drawBottomRightToLeft(
    canvas: ContentDrawScope,
    pointerOffset: Offset,
    page: Page
) {

    val size = canvas.size
    val blankOutline = page.blankOutline
    val foldPath = page.foldPath
    val clipPath = page.clipPath
    val pageOutline = page.pageOutline


    canvas.translate(size.width, size.height) {

        var startPoint = Offset(0F, 0F)

        var pointerPoint = Offset(
            pointerOffset.x - size.width,
            pointerOffset.y - size.height
        )

        // atan2斜率范围在 -PI到PI之间，因此第三象限为atan2 =  atan - PI, 那么atan = PI  + atan2
        val pointerRotate = atan2(pointerPoint.y - startPoint.y, pointerPoint.x - startPoint.x) + PI

        val _xLength = hypot(
            pointerPoint.x - startPoint.x,
            pointerPoint.y - startPoint.y
        ) / cos(pointerRotate);

        var xLength = 0F
        var yLength = 0F


        if (_xLength > size.width * 2) {
            //如果满足这个条件，意味着需要重新计算pointerPoint，因为没有形成垂直关系
            xLength = (size.width * 2);
            yLength = xLength / tan(pointerRotate).toFloat()

            var adjustRotate = atan(abs(yLength) / abs(xLength))

            val pointerDistance = abs(yLength * cos(adjustRotate))
            val y = abs(pointerDistance * sin(PI / 2 - adjustRotate))
            val x = abs(pointerDistance * cos(PI / 2 - adjustRotate))

            pointerPoint = Offset(
                -x.toFloat(),
                -y.toFloat()
            )
        } else {
            xLength = _xLength.toFloat()
            yLength = (xLength / tan(pointerRotate)).toFloat()
        }

        val XHalfAxisPoint = Offset(-xLength / 2F, 0F)
        val YHalfAxisPoint = Offset(0F, -yLength / 2F)

        val controlOffset = abs(Page.CONTROL_MAX_OFFSET * (2 * pointerPoint.x / size.width))

        val ld = Offset(
            (pointerPoint.x + XHalfAxisPoint.x) / 2F + controlOffset,
            (pointerPoint.y + XHalfAxisPoint.y) / 2F
        )
        val rt = Offset(
            (pointerPoint.x + YHalfAxisPoint.x) / 2F,
            (pointerPoint.y + YHalfAxisPoint.y) / 2F + controlOffset
        )

        val XControlAxisPoint = Offset(-xLength * 3 / 4F, 0F)
        val YControlfAxisPoint = Offset(0F, -yLength * 3 / 4F)


        foldPath.reset()
        foldPath.moveTo(XHalfAxisPoint.x, XHalfAxisPoint.y)
        foldPath.quadraticBezierTo(ld.x, ld.y, pointerPoint.x, pointerPoint.y)
        foldPath.quadraticBezierTo(rt.x, rt.y, YHalfAxisPoint.x, YHalfAxisPoint.y)
        foldPath.close()


        pageOutline.reset()
        pageOutline.moveTo(-size.width, -size.height)
        pageOutline.lineTo(-size.width, 0F)
        pageOutline.lineTo(0F, 0F)
        pageOutline.lineTo(0F, -size.height)
        pageOutline.close()

        blankOutline.reset()
        blankOutline.moveTo(0F, 0F)
        blankOutline.lineTo(YHalfAxisPoint.x, YHalfAxisPoint.y)
        blankOutline.lineTo(XHalfAxisPoint.x, XHalfAxisPoint.y)
        blankOutline.close()

        clipPath.reset()
        //剔除被裁剪的部分blankOutline
        clipPath.op(pageOutline, blankOutline, PathOperation.Difference)

        canvas.clipPath(clipPath) {
            canvas.translate(-size.width, -size.height) {
                canvas.drawRect(page.frontColor)
                canvas.drawContent()
            }
        }

        //绘制折角
        clipPath(foldPath) {
            //这里我们铺满把，就不旋转灰色背景了，反正都要裁剪
            canvas.drawRect(page.backColor, topLeft = Offset(-size.width, -size.height), size = Size(size.width, size.height))
            val t = atan2(pointerPoint.y, (pointerPoint.x - XHalfAxisPoint.x)) + PI
            //我们要把（XHalfAxisPoint.x,0）作为旋转中心，这里要计算新的夹角，但是在第三象限计算夹角需要做转换，转为第一象限便于计算，当然也可以使用atan

            // val degree = Math.toDegrees(t).toFloat()
            // Log.d(TAG, "drawBottomRightDragState degree = $degree")
            rotate(degrees = Math.toDegrees(t).toFloat(), pivot = Offset(XHalfAxisPoint.x, 0f)) { //图片按“露出”的1/2位置（XHalfAxisPoint.x,0f)）旋转
                page.imageBitmap?.let {
                    //由于原点在(size.width,size.height)，所以，x轴为负值，当然，图片展示在地下是不对的，需要和灰色背景一样往上移动size.height
                    // （我们这里使用的size.height，其实因为这里和image大小一样，理论上应该用image.width）
                    drawImage(it, Offset(-xLength + 0.5f, -size.height))
                }
            }
        }

        //绘制原点与触点的连线
        canvas.drawLine(start = Offset(0F, 0F), end = pointerPoint, color = Color.Red)
        //绘制切线
        canvas.drawLine(start = XHalfAxisPoint, end = YHalfAxisPoint, color = Color.Blue)
        //绘制1/2等距离切线
        canvas.drawLine(start = Offset(-xLength, 0F), end = Offset(0F, -yLength), color = Color.Blue)
        //绘制3/4等距离切线
        canvas.drawLine(start = XControlAxisPoint, end = YControlfAxisPoint, color = Color.Blue)

    }
}

private object Math {

    private const val RADIANS_TO_DEGREES: Double = 57.29577951308232

    fun toDegrees(angrad: Double): Double {
        return angrad * RADIANS_TO_DEGREES
    }
}

private fun drawTopRightToLeft(
    canvas: ContentDrawScope,
    pointerOffset: Offset,
    page: Page
) {
    val size = canvas.size
    val blankOutline = page.blankOutline
    val foldPath = page.foldPath
    val clipPath = page.clipPath
    val pageOutline = page.pageOutline

    canvas.translate(size.width, 0F) {

        var pointerPoint = Offset(
            pointerOffset.x - size.width,
            pointerOffset.y - 0
        )
        // atan2斜率范围在 -PI到PI之间，因此第三象限为atan2 =  atan - PI, 那么atan = PI  + atan2

        val startPoint = Offset(0F, 0F);

        val pointerRotate = atan2(pointerPoint.y - startPoint.y, pointerPoint.x - startPoint.x) + PI

        val _xLength = hypot(
            pointerPoint.x - startPoint.x,
            pointerPoint.y - startPoint.y
        ) / cos(pointerRotate)

        var xLength = 0F
        var yLength = 0F


        if (_xLength > size.width * 2.0) {
            //如果满足这个条件，意味着需要重新计算pointerPoint，因为没有形成垂直关系
            xLength = (size.width * 2);
            yLength = xLength / tan(pointerRotate).toFloat()

            var adjustRotate = atan(abs(yLength) / abs(xLength))

            val pointerDistance = abs(yLength * cos(adjustRotate))
            val y = abs(pointerDistance * sin(PI / 2 - adjustRotate))
            val x = abs(pointerDistance * cos(PI / 2 - adjustRotate))

            pointerPoint = Offset(
                -x.toFloat(),
                y.toFloat()
            )
        } else {
            xLength = _xLength.toFloat();
            yLength = xLength / tan(pointerRotate).toFloat()
        }

        val XHalfAxisPoint = Offset(-xLength / 2F, 0F)
        val YHalfAxisPoint = Offset(0F, -yLength / 2F)

        val controlOffset = abs(Page.CONTROL_MAX_OFFSET * (2 * pointerPoint.x / size.width))

        val ld = Offset(
            (pointerPoint.x + XHalfAxisPoint.x) / 2F + controlOffset,
            (pointerPoint.y + XHalfAxisPoint.y) / 2F
        )
        val rt = Offset(
            (pointerPoint.x + YHalfAxisPoint.x) / 2F,
            (pointerPoint.y + YHalfAxisPoint.y) / 2F - controlOffset
        )

        val XControlAxisPoint = Offset(-xLength * 3 / 4F, 0F)
        val YControlfAxisPoint = Offset(0F, -yLength * 3 / 4F)


        foldPath.reset()


        foldPath.moveTo(XHalfAxisPoint.x, XHalfAxisPoint.y)
        foldPath.quadraticBezierTo(ld.x, ld.y, pointerPoint.x, pointerPoint.y)
        foldPath.quadraticBezierTo(rt.x, rt.y, YHalfAxisPoint.x, YHalfAxisPoint.y)
        foldPath.close()


        foldPath.moveTo(XHalfAxisPoint.x, XHalfAxisPoint.y)
        foldPath.quadraticBezierTo(ld.x, ld.y, pointerPoint.x, pointerPoint.y)
        foldPath.quadraticBezierTo(rt.x, rt.y, YHalfAxisPoint.x, YHalfAxisPoint.y)
        foldPath.close()


        pageOutline.reset()
        pageOutline.moveTo(-size.width, 0F)
        pageOutline.lineTo(-size.width, size.height)
        pageOutline.lineTo(0F, size.height)
        pageOutline.lineTo(0F, 0F)
        pageOutline.close()

        blankOutline.reset()
        blankOutline.moveTo(0F, 0F)
        blankOutline.lineTo(YHalfAxisPoint.x, YHalfAxisPoint.y)
        blankOutline.lineTo(XHalfAxisPoint.x, XHalfAxisPoint.y)
        blankOutline.close()


        clipPath.reset()
        clipPath.op(pageOutline, blankOutline, PathOperation.Difference)
        canvas.clipPath(clipPath) {
            canvas.translate(-size.width, 0F) {
                canvas.drawRect(page.frontColor)
                canvas.drawContent()
            }
        }

        //绘制折角
        clipPath(foldPath) {
            //这里我们铺满把，就不旋转灰色背景了，反正都要裁剪
            canvas.drawRect(page.backColor, topLeft = Offset(-size.width, 0f), size = Size(size.width, size.height))
            val t = atan2(pointerPoint.y, (pointerPoint.x - XHalfAxisPoint.x)) + PI
            //我们要把（XHalfAxisPoint.x,0）作为旋转中心，这里要计算新的夹角，但是在第三象限计算夹角需要做转换，转为第一象限便于计算，当然也可以使用atan
            // val degree = Math.toDegrees(t).toFloat()
            //  Log.d(TAG,"drawTopRightDragState degree = $degree")
            rotate(degrees = Math.toDegrees(t).toFloat(), pivot = Offset(XHalfAxisPoint.x, 0f)) { //图片按“露出”的1/2位置（XHalfAxisPoint.x,0f)）旋转
                page.imageBitmap?.let {
                    //由于原点在(size.width,size.height)，所以，x轴为负值，当然，图片展示在地下是不对的，需要和灰色背景一样往上移动size.height
                    // （我们这里使用的size.height，其实因为这里和image大小一样，理论上应该用image.width）
                    drawImage(it, Offset(-xLength + 0.5f, 0f))
                }
            }
        }


        canvas.drawLine(start = Offset(0F, 0F), end = pointerPoint, color = Color.Red)
        canvas.drawLine(start = Offset(-xLength, 0F), end = Offset(0F, -yLength), color = Color.Blue)
        canvas.drawLine(start = XHalfAxisPoint, end = YHalfAxisPoint, color = Color.Blue)
        canvas.drawLine(start = XControlAxisPoint, end = YControlfAxisPoint, color = Color.Blue)

    }
}

@Stable
private class Page {

    var paint: Paint = Paint();

    var foldPath: Path = Path()

    var blankOutline = Path()

    var pageOutline = Path()

    var clipPath = Path()

    var frontColor = Color.White
    var backColor = Color.LightGray

    var snapshot = true

    var imageBitmap: ImageBitmap? = null;

    init {
        paint.style = PaintingStyle.Fill
        paint.color = Color.Red
        paint.isAntiAlias = true
    }

    companion object {
        const val STATE_IDLE = 0
        const val STATE_DRAGING_EXCEEDE = 1
        const val STATE_DRAGING_TOP = 2
        const val STATE_DRAGING_MIDDLE = 3
        const val STATE_DRAGING_BOTTOM = 4

        const val CONTROL_MAX_OFFSET = 40
    }
}