package com.example.kmmdemotest.demo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.cloudview.novel.readview.gesture.capture.CaptureController
import com.cloudview.novel.readview.gesture.capture.SimulateEffect
import com.cloudview.novel.readview.gesture.capture.capturable
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

/**
 * @author onuszhao
 * @since 2025/11/04
 * @description
 */

private val pageList = mutableListOf<Int>().apply {
    repeat(200) {
        add(it)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookViewDemo2() {
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

    val isFirst = index == 0
    val isLast = index == listState.layoutInfo.totalItemsCount - 1

    var prePageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    var curPageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    var nextPageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

    var viewSize by remember { mutableStateOf(IntSize.Zero) }
    val getSize = remember { { viewSize } }

    val scope = rememberCoroutineScope()
    val layer = rememberGraphicsLayer()

    val getLastBitMap = remember {
        {
            prePageBitmap.value ?: ImageBitmap(1, 1)
        }
    }
    val getCurrentBitMap = remember {
        {
            curPageBitmap.value ?: ImageBitmap(1, 1)
        }
    }
    val getNextBitMap = remember {
        {
            nextPageBitmap.value ?: ImageBitmap(1, 1)
        }
    }
    val simulateEffect = SimulateEffect(getSize, getLastBitMap, getCurrentBitMap, getNextBitMap)

    LogUtils.d("onuszhao", "SimulatePageView  prePageBitmap=$prePageBitmap  curPageBitmap=$curPageBitmap  nextPageBitmap=$nextPageBitmap")
    Box(
        // modifier = modifier
        // modifier = modifier.simulateMode(prePageBitmap.value, curPageBitmap.value,nextPageBitmap.value)
        modifier = simulateEffect.modifier.then(modifier).onSizeChanged {
            viewSize = it
        }
    ) {
        LogUtils.d("onuszhao", "SimulatePageView index=$index")
        PageView(
            index + 1, Modifier.fillMaxSize().zIndex(1f).background(bgColor)
                // .captureAsImageBitmap {
                //     nextPageBitmap.value = it
                // }
                .capturable(CaptureController(layer).also {
                    scope.launch {
                        prePageBitmap.value = it.captureAsync({}).await()
                    }
                })
        )

        val curEnable = !isLast
        PageView(
            index, Modifier.fillMaxSize().zIndex(2f).background(bgColor)
                // .captureAsImageBitmap {
                //     curPageBitmap.value = it
                // }
                .capturable(CaptureController(layer).also {
                    scope.launch {
                        curPageBitmap.value = it.captureAsync({}).await()
                    }
                })
        )

        val preZIndex = if (!isFirst) 3f else 0f // 默认为0，且值较大，后绘制，显示在最上层
        val preEnable = !isFirst
        PageView(
            index - 1, Modifier.fillMaxSize().zIndex(preZIndex).background(bgColor)
                // .captureAsImageBitmap {
                //     prePageBitmap.value = it
                // }
                .capturable(CaptureController(layer).also {
                    scope.launch {
                        nextPageBitmap.value = it.captureAsync({}).await()
                    }
                })
        )
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
private fun Modifier.simulateMode(
    prePageBitmap: ImageBitmap? = null,
    curPageBitmap: ImageBitmap? = null,
    nextPageBitmap: ImageBitmap? = null
): Modifier {
    LogUtils.d("onuszhao", "simulateMode prePageBitmap=$prePageBitmap  curPageBitmap=$curPageBitmap  nextPageBitmap=$nextPageBitmap")

    var touchOffset by remember { mutableStateOf(Offset.Zero) } //当前触摸点的位置
    var cornerOffset by remember { mutableStateOf(Offset.Zero) } //翻页的锚点
    var isRunning by remember { mutableStateOf(false) } //动画是否正在运行

    // val currentPageBitmap = remember { ImageBitmap(1, 1) } // 占位符
    // val nextPageBitmap = remember { ImageBitmap(1, 1) } // 占位符

    return this then
        pointerInput(Unit) {
            awaitEachGesture {
                val down = awaitFirstDown()
                touchOffset = down.position
                cornerOffset =
                    if (down.position.x > size.width / 2) Offset(size.width.toFloat(), size.height.toFloat())
                    else Offset(0f, size.height.toFloat())
                isRunning = true
                val dragResult = drag(down.id) { change ->
                    touchOffset = change.position
                    // 触发重绘
                }
                // 拖拽结束，触发动画
                isRunning = false
            }
        }
            // .pointerInput("touch") {
            //     var downX = 0f
            //     var downY = 0f
            //     var isDraggingAnim = false
            //
            //     awaitEachGesture {
            //         while (true) {
            //             val pointerEvent = awaitPointerEvent()
            //             if (pointerEvent.type == PointerEventType.Press) {
            //                 downX = pointerEvent.changes.first().position.x
            //                 downY = pointerEvent.changes.first().position.y
            //                 isDraggingAnim = false
            //                 moveDirection = PageDirection.NONE
            //                 dragState = Page.STATE_IDLE
            //                 pointerOffset = Offset(size.width.toFloat(), size.height.toFloat())
            //             } else if (pointerEvent.type == PointerEventType.Release) {
            //                 if (isDraggingAnim) {
            //                     needContinuAnim = true
            //                 }
            //                 break
            //             } else if (pointerEvent.type == PointerEventType.Move) {
            //                 val upPosition = pointerEvent.changes.first().position
            //
            //                 if (isDraggingAnim) {
            //                     pointerOffset = upPosition
            //                 } else {
            //                     val moveX = upPosition.x - downX
            //                     val touchSlop = localConfig.touchSlop
            //                     if (abs(moveX) > 0) {
            //                         isDraggingAnim = abs(moveX) > touchSlop
            //                         when {
            //                             moveX > 0 -> { // 右滑: 向右移动
            //                                 moveDirection = if (abs(moveX) > touchSlop) PageDirection.PREV else PageDirection.PREV_PREPARE
            //                                 dragState = if (abs(moveX) > touchSlop) Page.STATE_DRAGING_MIDDLE else Page.STATE_DRAGING_EXCEEDE
            //                                 pointerOffset = upPosition
            //                             }
            //
            //                             moveX < 0 -> { // 左滑: 向左移动
            //                                 moveDirection = if (abs(moveX) > touchSlop) PageDirection.NEXT else PageDirection.NEXT_PREPARE
            //                                 dragState = if (abs(moveX) > touchSlop) {
            //                                     if (upPosition.y < size.height / 3) {
            //                                         Page.STATE_DRAGING_TOP
            //                                     } else if (upPosition.y > size.height * 2 / 3) {
            //                                         Page.STATE_DRAGING_BOTTOM
            //                                     } else {
            //                                         Page.STATE_DRAGING_MIDDLE
            //                                     }
            //                                 } else {
            //                                     Page.STATE_DRAGING_EXCEEDE
            //                                 }
            //                                 pointerOffset = upPosition
            //                             }
            //
            //                             else -> {
            //                                 dragState = Page.STATE_DRAGING_EXCEEDE
            //                             }
            //                         }
            //                     }
            //                 }
            //             }
            //         }
            //     }
            // }
            .drawWithContent {
                // 绘制原始内容
                // drawContent()

                // if (!isRunning || curPageBitmap == null || nextPageBitmap == null) {
                //     drawContent()
                //     return@drawWithContent
                // }

                // 将原生代码中的所有计算逻辑移植到这里
                // val (mBezierStart1, mBezierControl1, mBezierEnd1, mBezierVertex1) = calculateBezierPoints(touchOffset, cornerOffset, size)
                // val (mBezierStart2, mBezierControl2, mBezierEnd2, mBezierVertex2) = calculateBezierPoints(touchOffset, cornerOffset, size, isSecondary = true)
                //
                // // drawCurr
                // // entPageArea: 绘制当前页的非翻折部分
                // drawCurrentPageArea(
                //     curPageBitmap,
                //     mBezierStart1,
                //     mBezierControl1,
                //     mBezierEnd1,
                //     touchOffset,
                //     mBezierEnd2,
                //     mBezierControl2,
                //     mBezierStart2,
                //     cornerOffset
                // )
                //
                // // drawNextPageAreaAndShadow: 绘制下一页内容和背面阴影
                // drawNextPageAreaAndShadow(nextPageBitmap, mBezierStart1, mBezierVertex1, mBezierVertex2, mBezierStart2, cornerOffset)
                //
                // // drawCurrentPageShadow: 绘制翻起页的阴影
                // drawCurrentPageShadow(touchOffset, mBezierControl1, mBezierStart1, mBezierControl2, mBezierStart2)
            }
}

private fun ContentDrawScope.drawContentWithLayer(canvas: ContentDrawScope, layer: GraphicsLayer) {
    layer.record {
        canvas.drawContent()
    }
    drawLayer(layer)
}

private fun drawContent(canvas: ContentDrawScope) {
    canvas.drawContent()
}

/**
 * 计算贝塞尔曲线的关键点。
 * 这是一个简化的示例，你需要将原生代码中所有复杂的几何计算逻辑都迁移进来。
 */
private fun calculateBezierPoints(
    touch: Offset,
    corner: Offset,
    size: androidx.compose.ui.geometry.Size,
    isSecondary: Boolean = false
): QuadPoints {
    // 假设这是原生代码中 calcPoints 的简化版
    val middleX = (touch.x + corner.x) / 2
    val middleY = (touch.y + corner.y) / 2

    val bezierControl1 = Offset(
        x = middleX - (corner.y - middleY) * (corner.y - middleY) / (corner.x - middleX),
        y = corner.y
    )

    val bezierControl2 = Offset(
        x = corner.x,
        y = middleY - (corner.x - middleX) * (corner.x - middleX) / (corner.y - middleY)
    )

    val bezierStart1 = Offset(
        x = bezierControl1.x - (corner.x - bezierControl1.x) / 2,
        y = corner.y
    )

    val bezierStart2 = Offset(
        x = corner.x,
        y = bezierControl2.y - (corner.y - bezierControl2.y) / 2
    )

    val bezierEnd1 = getCross(touch, bezierControl1, bezierStart1, bezierStart2)
    val bezierEnd2 = getCross(touch, bezierControl2, bezierStart1, bezierStart2)

    val bezierVertex1 = Offset(
        x = (bezierStart1.x + 2 * bezierControl1.x + bezierEnd1.x) / 4,
        y = (2 * bezierControl1.y + bezierStart1.y + bezierEnd1.y) / 4
    )
    val bezierVertex2 = Offset(
        x = (bezierStart2.x + 2 * bezierControl2.x + bezierEnd2.x) / 4,
        y = (2 * bezierControl2.y + bezierStart2.y + bezierEnd2.y) / 4
    )

    return if (isSecondary) {
        QuadPoints(bezierStart2, bezierControl2, bezierEnd2, bezierVertex2)
    } else {
        QuadPoints(bezierStart1, bezierControl1, bezierEnd1, bezierVertex1)
    }
}

private data class QuadPoints(val start: Offset, val control: Offset, val end: Offset, val vertex: Offset)

/**
 * 求解直线P1P2和直线P3P4的交点坐标
 */
private fun getCross(P1: Offset, P2: Offset, P3: Offset, P4: Offset): Offset {
    val a1 = (P2.y - P1.y) / (P2.x - P1.x)
    val b1 = (P1.x * P2.y - P2.x * P1.y) / (P1.x - P2.x)
    val a2 = (P4.y - P3.y) / (P4.x - P3.x)
    val b2 = (P3.x * P4.y - P4.x * P3.y) / (P3.x - P4.x)
    val crossX = (b2 - b1) / (a1 - a2)
    val crossY = a1 * crossX + b1
    return Offset(crossX, crossY)
}

/**
 * 绘制当前页的非翻折部分。
 */
private fun DrawScope.drawCurrentPageArea(
    bitmap: ImageBitmap,
    s1: Offset, c1: Offset, e1: Offset,
    touch: Offset, e2: Offset, c2: Offset, s2: Offset, corner: Offset
) {
    val path = Path().apply {
        moveTo(s1.x, s1.y)
        quadraticBezierTo(c1.x, c1.y, e1.x, e1.y)
        lineTo(touch.x, touch.y)
        lineTo(e2.x, e2.y)
        quadraticBezierTo(c2.x, c2.y, s2.x, s2.y)
        lineTo(corner.x, corner.y)
        close()
    }
    // 使用 clipPath 裁剪出未翻折的区域
    clipPath(path, clipOp = ClipOp.Difference) {
        drawImage(bitmap, topLeft = Offset.Zero)
    }
}

/**
 * 绘制下一页内容和背面阴影。
 */
private fun DrawScope.drawNextPageAreaAndShadow(
    bitmap: ImageBitmap,
    s1: Offset, v1: Offset, v2: Offset, s2: Offset, corner: Offset
) {
    val path = Path().apply {
        moveTo(s1.x, s1.y)
        lineTo(v1.x, v1.y)
        lineTo(v2.x, v2.y)
        lineTo(s2.x, s2.y)
        lineTo(corner.x, corner.y)
        close()
    }
    clipPath(path, clipOp = ClipOp.Intersect) {
        drawImage(bitmap, topLeft = Offset.Zero)
    }
    // 绘制阴影的逻辑需要更复杂的计算，这里简化
    // TODO: 迁移原生代码中的阴影绘制逻辑
}

/**
 * 绘制翻起页的阴影。
 */
private fun DrawScope.drawCurrentPageShadow(
    touch: Offset, c1: Offset, s1: Offset, c2: Offset, s2: Offset
) {
    // 绘制阴影的逻辑需要更复杂的计算，这里简化
    // TODO: 迁移原生代码中的阴影绘制逻辑
}