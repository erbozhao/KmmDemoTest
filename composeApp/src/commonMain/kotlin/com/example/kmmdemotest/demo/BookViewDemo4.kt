package com.example.kmmdemotest.demo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.kmmdemotest.ext.NoOpOverscrollEffect
import com.example.kmmdemotest.ext.rememberReadViewSnapFlingBehavior
import com.example.kmmdemotest.utils.ImageUtils
import com.example.kmmdemotest.utils.LogUtils
import kmmdemotest.composeapp.generated.resources.Res
import kmmdemotest.composeapp.generated.resources.img_beauty
import kmmdemotest.composeapp.generated.resources.img_checken
import kmmdemotest.composeapp.generated.resources.img_mountain
import kotlinx.coroutines.CoroutineScope
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
 * @author onuszhao
 * @since 2025/11/04
 * @description
 */

private val pageList = mutableListOf<Int>().apply {
    repeat(6) {
        add(it)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookViewDemo4() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        val bgColor = Color(0xFFF8F1E7) // 0xFFF8F1E7 0xFFF6F6F6  0xFFE0F1DE 0xFFD9EBFF 0xFFFEEEFF

        LazyRow(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            flingBehavior = rememberReadViewSnapFlingBehavior(listState),
            overscrollEffect = NoOpOverscrollEffect(),
            userScrollEnabled = false,
        ) {
            itemsIndexed(pageList) { index, item ->
                SimulatePageView(
                    index = index,
                    modifier = Modifier.fillParentMaxSize(),
                    bgColor = bgColor,
                    scope = scope,
                    listState = listState
                )
            }
        }

    }
}

@Composable
private fun PageView(index: Int, modifier: Modifier) {
    val position = index % 3
    val drawableResource = if (position == 0) Res.drawable.img_mountain else if (position == 1) Res.drawable.img_beauty else Res.drawable.img_checken
    val contentText = if (position == 0) {
        "\t\t他清楚的知道，这个实验成功的概率是多么的低，他望着窗台的透进来的晨光，内心无比的焦灼，这才是早上九点种，但他仿佛看到了落日的余晖。" + "\n\t\t病床的男人抽搐不停，他已经没有多长时间了，长期的抽搐，导致他无法入眠，如果这种状态再延续下去，走向人生的重点已成必然。" + "\n\t\t梁雨，你有什么遗言么？" + "\n\t\t他从窗台方向转向过来，看见他的初中老同学张铭生。" + "\n\t\t我能有什么遗言，孤家寡人而已!" + "\n\t\t嗯～啊？不想给张桐说几句么？听说她离婚了" + "\n\t\t她说有很多话要对你说"
    } else if (position == 1) {
        "\t\t我为什么要关心她？" + "\n\t\t你曾经说过，此生只爱她一个人的？因此你一直单身，对吧！" + "\n\t\t梁医生，你忘了？" + "\n\t\t我不是医生，我只是个打工仔，我也没有忘记，但是那份爱已经不会再有了" + "\n\t\t嗨，她可是主动让我找你哦！" + "\n\t\t听说，她小孩生病了！——张铭生说到。" + "\n\t\t她真会找时间，她永远会在最困难的时候找我，永远会在没有困难的时候离我而去。" + "\n\t\t事实或许相反，她离开你时已经是迫不得已，张铭生调高嗓门说到。" + "\n\t\t"
    } else {
        "她站在光影交错的午后，一袭淡雅的裙装如烟雾般轻柔。她的容颜，美得仿佛是从古典油画中走出来的缪斯。\n" +
            "\n" +
            "最引人注目的是那一双眼眸，宛如两潭深秋的湖水，清澈而深邃，眼波流转间，带着一种令人心动的灵气与柔情。高挺的鼻梁下，是两瓣樱花般的唇，不施粉黛也自成风情，微微上扬时，露出一丝若有若无的笑意，甜而不腻。\n" +
            "\n" +
            "她的肌肤白皙如玉，在阳光下泛着细腻的光泽，颈项修长，线条优美。乌黑的长发慵懒地披散，偶尔被微风拂动，带起一阵淡雅的馨香。她的一举一动都透着高贵与从容，那份由内而外散发出的自信与优雅，比外表的惊艳更摄人心魄，让人看一眼便再难忘怀。她不是那种咄咄逼人的美，而是温柔且坚定的，像夜空中的明月，清冷却又温暖。"
    }

    Column(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center,
            painter = painterResource(drawableResource),
            contentDescription = ""
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            text = "$index-$index-$index-$index-$index-$index \n$contentText"
        )
    }
}

@Composable
private fun SimulatePageView(index: Int, modifier: Modifier, bgColor: Color, scope: CoroutineScope, listState: LazyListState) {

    var prePageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    var curPageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    var nextPageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

    val scope = rememberCoroutineScope()

    val isFirst = index == 0
    val isLast = index == listState.layoutInfo.totalItemsCount - 1

    Box(
        modifier = modifier
            .simulateMode4(prePageBitmap.value, curPageBitmap.value, nextPageBitmap.value) { moveDirection ->
                LogUtils.d("onuszhao", "SimulatePageView end index=$index  direction=${moveDirection}")
                scope.launch {
                    if (moveDirection == PageDirection4.PREV && !isFirst) {
                        listState.scrollToItem(index - 1)
                    } else if (moveDirection == PageDirection4.NEXT && !isLast) {
                        listState.scrollToItem(index + 1)
                    }
                }
            }
    ) {
        LogUtils.d("onuszhao", "SimulatePageView init index=$index")
        val nextModifier = if (nextPageBitmap.value == null) Modifier.fillMaxSize().zIndex(1f).background(bgColor)
            .captureAsImageBitmap { nextPageBitmap.value = it } else Modifier.fillMaxSize().zIndex(1f).background(bgColor)
        PageView(index + 1, nextModifier)

        val curModifier = if (nextPageBitmap.value == null) Modifier.fillMaxSize().zIndex(2f).background(bgColor)
            .captureAsImageBitmap { nextPageBitmap.value = it } else Modifier.fillMaxSize().zIndex(2f).background(bgColor)
        PageView(index, curModifier)

        val preZIndex = if (!isFirst) 3f else 0f // 默认为0，且值较大，后绘制，显示在最上层
        val preModifier = if (nextPageBitmap.value == null) Modifier.fillMaxSize().zIndex(preZIndex).background(bgColor)
            .captureAsImageBitmap { nextPageBitmap.value = it } else Modifier.fillMaxSize().zIndex(preZIndex).background(bgColor)
        PageView(index - 1, preModifier)
    }
}

private fun Modifier.captureAsImageBitmap(onImageCaptured: (ImageBitmap) -> Unit): Modifier =
    this.composed {
        val layer = rememberGraphicsLayer()
        val scope = rememberCoroutineScope()
        val density = LocalDensity.current

        // Use a key to recompose when the content changes
        val captureModifier = Modifier.drawWithContent {
            // 先绘制内容，并同步绘制到layer上
            layer.record {
                // this@drawWithContent.drawRect(page.frontColor)
                this@drawWithContent.drawContent()
            }
            drawLayer(layer)

            // 开始截图
            scope.launch {
                val imageBitmap = ImageUtils.convertSoftwareBitmap(layer.toImageBitmap())
                val snapImageBitmap = ImageBitmap(imageBitmap.width, imageBitmap.height, ImageBitmapConfig.Argb8888)
                // val snapImageBitmap = ImageUtils.copyToNewImageBitmap(imageBitmap)!!
                val snapshotCanvas = Canvas(snapImageBitmap)
                // val frontColor = page.frontColor
                // page.frontColor = Color.Transparent

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
                // page.imageBitmap = snapImageBitmap
                // page.frontColor = frontColor

                // this.drawContext
                onImageCaptured(imageBitmap)
                // drawContent()
            }

        }
        captureModifier
    }

@Composable
private fun Modifier.simulateMode4(
    prePageBitmap: ImageBitmap? = null,
    curPageBitmap: ImageBitmap? = null,
    nextPageBitmap: ImageBitmap? = null,
    onMoveEnd: (PageDirection4) -> Unit
): Modifier {

    LogUtils.d("onuszhao", "simulateMode4 prePageBitmap=$prePageBitmap  curPageBitmap=$curPageBitmap  nextPageBitmap=$nextPageBitmap")

    var moveDirection by remember { mutableStateOf(PageDirection4.NONE) }
    var pointerOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    var dragState by remember { mutableIntStateOf(Page4.Companion.STATE_IDLE) }

    val localConfig = LocalViewConfiguration.current

    return this then
        pointerInput("touch") {
            var downX = 0f
            var downY = 0f
            var isDraggingAnim = false

            awaitEachGesture {
                while (true) {
                    val pointerEvent = awaitPointerEvent()
                    if (pointerEvent.type == PointerEventType.Press) {
                        downX = pointerEvent.changes.first().position.x
                        downY = pointerEvent.changes.first().position.y
                        isDraggingAnim = false
                        moveDirection = PageDirection4.NONE
                        dragState = Page4.Companion.STATE_IDLE
                        pointerOffset = Offset(size.width.toFloat(), size.height.toFloat())
                    } else if (pointerEvent.type == PointerEventType.Release) {
                        if (isDraggingAnim) {
                            onMoveEnd.invoke(moveDirection)
                        }
                        break
                    } else if (pointerEvent.type == PointerEventType.Move) {
                        val upPosition = pointerEvent.changes.first().position

                        if (isDraggingAnim) {
                            pointerOffset = upPosition
                        } else {
                            val moveX = upPosition.x - downX
                            val touchSlop = localConfig.touchSlop
                            if (abs(moveX) > 0) {
                                isDraggingAnim = abs(moveX) > touchSlop
                                when {
                                    moveX > 0 -> { // 右滑: 向右移动
                                        moveDirection = if (abs(moveX) > touchSlop) PageDirection4.PREV else PageDirection4.PREV_PREPARE
                                        dragState =
                                            if (abs(moveX) > touchSlop) Page4.Companion.STATE_DRAGING_MIDDLE else Page4.Companion.STATE_DRAGING_EXCEEDE
                                        pointerOffset = upPosition
                                    }

                                    moveX < 0 -> { // 左滑: 向左移动
                                        moveDirection = if (abs(moveX) > touchSlop) PageDirection4.NEXT else PageDirection4.NEXT_PREPARE
                                        dragState = if (abs(moveX) > touchSlop) {
                                            if (upPosition.y < size.height / 3) {
                                                Page4.Companion.STATE_DRAGING_TOP
                                            } else if (upPosition.y > size.height * 2 / 3) {
                                                Page4.Companion.STATE_DRAGING_BOTTOM
                                            } else {
                                                Page4.Companion.STATE_DRAGING_MIDDLE
                                            }
                                        } else {
                                            Page4.Companion.STATE_DRAGING_EXCEEDE
                                        }
                                        pointerOffset = upPosition
                                    }

                                    else -> {
                                        dragState = Page4.Companion.STATE_DRAGING_EXCEEDE
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
            .drawWithContent {
                // LogUtils.d(
                //     "onuszhao",
                //     "drawWithContent  dragState=$dragState  pointerOffset=${pointerOffset} snapshot=${page.snapshot}  image=${page.imageBitmap}  width=${size.width}  layerWidth=${layer.size.width}"
                // )

                when (dragState) {
                    Page4.Companion.STATE_DRAGING_TOP -> {
                        drawTopRightRightDragState4(this, pointerOffset, prePageBitmap, curPageBitmap)
                    }

                    Page4.Companion.STATE_DRAGING_BOTTOM -> {
                        drawBottomRightDragState4(this, pointerOffset, prePageBitmap, curPageBitmap)
                    }

                    Page4.Companion.STATE_DRAGING_MIDDLE -> {
                        drawMiddleDragState4(this, pointerOffset, prePageBitmap, curPageBitmap)
                    }

                    else -> {
                        drawIdleState(this)
                    }
                }

            }
}

private suspend fun snapShotIfNeed(page: Page4, layer: GraphicsLayer, canvasWidth: Int) {
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
    page: Page4,
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
) {
    // canvas.drawRect(page.frontColor)
    canvas.drawContent()
}

private fun drawMiddleDragState4(
    canvas: ContentDrawScope,
    pointerOffset: Offset,
    preImageBitmap: ImageBitmap? = null,
    curImageBitmap: ImageBitmap? = null
) {
    val size = canvas.size
    val foldPath = Path()
    val pageOutline = Path()
    val blankOutline = Path()
    val clipPath = Path()

    canvas.translate(size.width, 0F) {
        // 1. 计算在平移坐标系中的关键点
        val pointerPoint = Offset(
            pointerOffset.x - size.width,
            pointerOffset.y - 0
        )

        val verticalPoint = Offset(pointerPoint.x, 0F)
        val halfVerticalPoint = Offset(pointerPoint.x / 2F, 0F)

        // 2. 重新定义折叠和裁剪路径

        // foldPath: 折叠部分在背面 (用于绘制折叠页面的内容/背面颜色)
        foldPath.reset()
        foldPath.moveTo(verticalPoint.x, verticalPoint.y)
        foldPath.lineTo(halfVerticalPoint.x, halfVerticalPoint.y)
        foldPath.lineTo(halfVerticalPoint.x, size.height)
        foldPath.lineTo(verticalPoint.x, size.height)
        foldPath.close()

        // pageOutline: 整个页面区域
        pageOutline.reset()
        pageOutline.moveTo(-size.width, 0F)
        pageOutline.lineTo(-size.width, size.height)
        pageOutline.lineTo(0F, size.height)
        pageOutline.lineTo(0F, 0F)
        pageOutline.close()

        // blankOutline: 中间被挖空的矩形区域 (折叠部分所占据的空白区域)
        blankOutline.reset()
        blankOutline.moveTo(0F, 0F)
        blankOutline.lineTo(halfVerticalPoint.x, halfVerticalPoint.y)
        blankOutline.lineTo(halfVerticalPoint.x, size.height)
        blankOutline.lineTo(0F, size.height)
        blankOutline.close()

        // clipPath: 未折叠部分的页面区域 (pageOutline - blankOutline)
        clipPath.reset()
        clipPath.op(pageOutline, blankOutline, PathOperation.Difference)

        // 3. 绘制未折叠页面部分 (正面内容)
        canvas.withTransform({
            clipPath(clipPath)
            translate(-size.width, 0F)
        }) {
            // canvas.drawRect(page.frontColor)
            canvas.drawContent()
        }

        // 4. 绘制折叠页面部分 (背面内容)
        clipPath(foldPath) {
            // canvas.drawRect(page.backColor, topLeft = Offset(verticalPoint.x, 0f), size = Size(size.width, size.height))
            curImageBitmap?.let {
                drawImage(it, Offset(verticalPoint.x, 0f))
            }
        }

        // 5. 绘制阴影
        val SHADOW_WIDTH = 20f
        val foldX = verticalPoint.x // 主折痕 X 坐标
        val reflectX = halfVerticalPoint.x // 反射边 X 坐标

        // 5. 绘制主折痕阴影 (在未折叠页面上，从右向左渐变)
        // 阴影从 foldX 处的深色渐变到 foldX - SHADOW_WIDTH 处的透明
        val mainFoldShadowBrush = Brush.linearGradient(
            colors = listOf(
                Color.Black.copy(alpha = 0.5f), // 紧邻折痕处较深
                Color.Transparent
            ),
            start = Offset(foldX, 0f),
            end = Offset(foldX - SHADOW_WIDTH, 0f)
        )

        canvas.drawRect(
            brush = mainFoldShadowBrush,
            topLeft = Offset(foldX - SHADOW_WIDTH, 0f),
            size = Size(SHADOW_WIDTH, size.height)
        )

        // 6. 绘制反射边阴影 (在空白/背景上，从左向右渐变)
        // 阴影从 reflectX 处的深色渐变到 reflectX + SHADOW_WIDTH 处的透明
        val reflectShadowBrush = Brush.linearGradient(
            colors = listOf(
                Color.Black.copy(alpha = 0.3f), // 紧邻反射边处较深
                Color.Transparent
            ),
            start = Offset(reflectX, 0f),
            end = Offset(reflectX + SHADOW_WIDTH, 0f)
        )

        canvas.drawRect(
            brush = reflectShadowBrush,
            topLeft = Offset(reflectX, 0f),
            size = Size(SHADOW_WIDTH, size.height)
        )

        // 7. 调试线 - 替换为阴影后通常可以移除
        // canvas.drawLine(start = Offset(size.width, pointerPoint.y), end = pointerPoint, color = Color.Red)
        // canvas.drawLine(start = halfVerticalPoint, end = Offset(halfVerticalPoint.x, size.height), color = Color.Blue)
        // canvas.drawLine(start = verticalPoint, end = Offset(verticalPoint.x, size.height), color = Color.Blue)
    }
}

private fun drawBottomRightDragState4(
    canvas: ContentDrawScope,
    pointerOffset: Offset,
    preImageBitmap: ImageBitmap? = null,
    curImageBitmap: ImageBitmap? = null
) {
    val size = canvas.size
    val blankOutline = Path()
    val foldPath = Path()
    val clipPath = Path()
    val pageOutline = Path()


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

        val controlOffset = abs(Page4.Companion.CONTROL_MAX_OFFSET * (2 * pointerPoint.x / size.width))

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
                // canvas.drawRect(page.frontColor)
                canvas.drawContent()
            }
        }

        //绘制折角
        clipPath(foldPath) {
            //这里我们铺满把，就不旋转灰色背景了，反正都要裁剪
            // canvas.drawRect(page.backColor, topLeft = Offset(-size.width, -size.height), size = Size(size.width, size.height))
            val t = atan2(pointerPoint.y, (pointerPoint.x - XHalfAxisPoint.x)) + PI
            //我们要把（XHalfAxisPoint.x,0）作为旋转中心，这里要计算新的夹角，但是在第三象限计算夹角需要做转换，转为第一象限便于计算，当然也可以使用atan

            // val degree = Math.toDegrees(t).toFloat()
            // Log.d(TAG, "drawBottomRightDragState4 degree = $degree")
            rotate(degrees = Math4.toDegrees(t).toFloat(), pivot = Offset(XHalfAxisPoint.x, 0f)) { //图片按“露出”的1/2位置（XHalfAxisPoint.x,0f)）旋转
                curImageBitmap?.let {
                    //由于原点在(size.width,size.height)，所以，x轴为负值，当然，图片展示在地下是不对的，需要和灰色背景一样往上移动size.height
                    // （我们这里使用的size.height，其实因为这里和image大小一样，理论上应该用image.width）
                    drawImage(it, Offset(-xLength + 0.5f, -size.height))
                }
            }
        }

        // 绘制折角边线阴影和边线
        // 1. 定义描边样式
        val edgeStrokeWidth = 4.dp.toPx() // 边线宽度
        val shadowWidth = 6.dp.toPx()     // 阴影宽度 (比边线宽)
        val shadowOffset = Offset(2f, 2f)  // 阴影偏移量

        // 2. 绘制阴影 (使用更粗、略微偏移、半透明的路径来模拟)
        canvas.drawPath(
            path = foldPath,
            color = Color.Black.copy(alpha = 0.3f), // 阴影颜色和透明度
            style = Stroke(
                width = shadowWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            ),
            // 将画布移动以创建阴影偏移效果
            // 注意: 在translate作用域内, 这是一个局部平移, 影响drawPath的绘制位置
            // 您也可以直接在drawPath前后的canvas.translate/withTransform中进行操作
            // 但直接在当前位置绘制一个偏移的路径更简单
        )

        // 另一种实现偏移的方法 (更直接):
        canvas.translate(shadowOffset.x, shadowOffset.y) {
            canvas.drawPath(
                path = foldPath,
                color = Color.Black.copy(alpha = 0.3f), // 阴影颜色和透明度
                style = Stroke(
                    width = shadowWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // 3. 绘制清晰的边线 (覆盖在阴影之上)
        canvas.drawPath(
            path = foldPath,
            color = Color.Gray, // 边线颜色
            style = Stroke(
                width = edgeStrokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // 调试线
        // canvas.drawLine(start = Offset(0F, 0F), end = pointerPoint, color = Color.Red)        //绘制原点与触点的连线
        // canvas.drawLine(start = XHalfAxisPoint, end = YHalfAxisPoint, color = Color.Blue)         //绘制切线
        // canvas.drawLine(start = Offset(-xLength, 0F), end = Offset(0F, -yLength), color = Color.Blue)      //绘制1/2等距离切线
        // canvas.drawLine(start = XControlAxisPoint, end = YControlfAxisPoint, color = Color.Blue)         //绘制3/4等距离切线
    }
}

private object Math4 {

    const val RADIANS_TO_DEGREES: Double = 57.29577951308232

    fun toDegrees(angrad: Double): Double {
        return angrad * RADIANS_TO_DEGREES
    }
}

private fun drawTopRightRightDragState4(
    canvas: ContentDrawScope,
    pointerOffset: Offset,
    preImageBitmap: ImageBitmap? = null,
    curImageBitmap: ImageBitmap? = null
) {
    val size = canvas.size
    val blankOutline = Path()
    val foldPath = Path()
    val clipPath = Path()
    val pageOutline = Path()

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

        val controlOffset = abs(Page4.Companion.CONTROL_MAX_OFFSET * (2 * pointerPoint.x / size.width))

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

        // 1. 定义折叠路径 (用于裁剪和绘制背面)
        foldPath.reset()
        foldPath.moveTo(XHalfAxisPoint.x, XHalfAxisPoint.y)
        foldPath.quadraticBezierTo(ld.x, ld.y, pointerPoint.x, pointerPoint.y)
        foldPath.quadraticBezierTo(rt.x, rt.y, YHalfAxisPoint.x, YHalfAxisPoint.y)
        foldPath.close()

        // 2. 定义页面轮廓
        pageOutline.reset()
        pageOutline.moveTo(-size.width, 0F)
        pageOutline.lineTo(-size.width, size.height)
        pageOutline.lineTo(0F, size.height)
        pageOutline.lineTo(0F, 0F)
        pageOutline.close()

        // 3. 定义空白区域 (被折叠部分遮盖的三角区域)
        blankOutline.reset()
        blankOutline.moveTo(0F, 0F)
        blankOutline.lineTo(YHalfAxisPoint.x, YHalfAxisPoint.y)
        blankOutline.lineTo(XHalfAxisPoint.x, XHalfAxisPoint.y)
        blankOutline.close()

        // 4. 定义裁剪路径 (未折叠页面区域)
        clipPath.reset()
        clipPath.op(pageOutline, blankOutline, PathOperation.Difference)

        // --- 绘制未折叠页面部分 (正面内容) ---
        canvas.clipPath(clipPath) {
            canvas.translate(-size.width, 0F) {
                // canvas.drawRect(page.frontColor)
                canvas.drawContent()
            }
        }

        // --- 绘制折角 (背面内容) ---
        clipPath(foldPath) {
            // 绘制背面颜色
            //这里我们铺满把，就不旋转灰色背景了，反正都要裁剪
            // canvas.drawRect(page.backColor, topLeft = Offset(-size.width, 0f), size = Size(size.width, size.height))

            // 绘制背面图片 (已旋转)
            val t = atan2(pointerPoint.y, (pointerPoint.x - XHalfAxisPoint.x)) + PI
            //我们要把（XHalfAxisPoint.x,0）作为旋转中心，这里要计算新的夹角，但是在第三象限计算夹角需要做转换，转为第一象限便于计算，当然也可以使用atan
            // val degree = Math.toDegrees(t).toFloat()
            //  Log.d(TAG,"drawTopRightDragState degree = $degree")
            rotate(degrees = Math4.toDegrees(t).toFloat(), pivot = Offset(XHalfAxisPoint.x, 0f)) { //图片按“露出”的1/2位置（XHalfAxisPoint.x,0f)）旋转
                curImageBitmap?.let {
                    //由于原点在(size.width,size.height)，所以，x轴为负值，当然，图片展示在地下是不对的，需要和灰色背景一样往上移动size.height
                    // （我们这里使用的size.height，其实因为这里和image大小一样，理论上应该用image.width）
                    drawImage(it, Offset(-xLength + 0.5f, 0f))
                }
            }
        }

        // 绘制折角边线阴影和边线
        // 1. 定义描边样式
        val edgeStrokeWidth = 4.dp.toPx() // 边线宽度
        val shadowWidth = 6.dp.toPx()     // 阴影宽度 (比边线宽)
        val shadowOffset = Offset(2f, 2f)  // 阴影偏移量

        // 2. 绘制阴影 (使用更粗、略微偏移、半透明的路径来模拟)
        canvas.drawPath(
            path = foldPath,
            color = Color.Black.copy(alpha = 0.3f), // 阴影颜色和透明度
            style = Stroke(
                width = shadowWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            ),
            // 将画布移动以创建阴影偏移效果
            // 注意: 在translate作用域内, 这是一个局部平移, 影响drawPath的绘制位置
            // 您也可以直接在drawPath前后的canvas.translate/withTransform中进行操作
            // 但直接在当前位置绘制一个偏移的路径更简单
        )

        // 另一种实现偏移的方法 (更直接):
        canvas.translate(shadowOffset.x, shadowOffset.y) {
            canvas.drawPath(
                path = foldPath,
                color = Color.Black.copy(alpha = 0.3f), // 阴影颜色和透明度
                style = Stroke(
                    width = shadowWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // 3. 绘制清晰的边线 (覆盖在阴影之上)
        canvas.drawPath(
            path = foldPath,
            color = Color.Gray, // 边线颜色
            style = Stroke(
                width = edgeStrokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // 调试线
        // canvas.drawLine(start = Offset(0F, 0F), end = pointerPoint, color = Color.Red)
        // canvas.drawLine(start = Offset(-xLength, 0F), end = Offset(0F, -yLength), color = Color.Blue)
        // canvas.drawLine(start = XHalfAxisPoint, end = YHalfAxisPoint, color = Color.Blue)
        // canvas.drawLine(start = XControlAxisPoint, end = YControlfAxisPoint, color = Color.Blue)
    }
}

@Stable
private class Page4 {

    var foldPath: Path = Path()

    var blankOutline = Path()

    var pageOutline = Path()

    var clipPath = Path()

    var frontColor = Color.White
    var backColor = Color.LightGray

    var snapshot = true

    var imageBitmap: ImageBitmap? = null;

    companion object Companion {
        const val STATE_IDLE = 0
        const val STATE_DRAGING_EXCEEDE = 1
        const val STATE_DRAGING_TOP = 2
        const val STATE_DRAGING_MIDDLE = 3
        const val STATE_DRAGING_BOTTOM = 4

        const val CONTROL_MAX_OFFSET = 40
    }
}

private enum class PageDirection4 {
    PREV_PREPARE,
    PREV,
    NONE,
    NEXT_PREPARE,
    NEXT
}