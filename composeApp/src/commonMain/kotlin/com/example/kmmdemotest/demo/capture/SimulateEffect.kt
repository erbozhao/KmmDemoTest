package com.cloudview.novel.readview.gesture.capture

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.sin

/**
 * CloudView limited 2023 copyright.
 *
 * @Description: 模拟翻页特效
 * @User: tonysheng
 * @Date: 2024/10/30
 * @Time: 19:54
 * @version: V1.0
 */
class SimulateEffect(
    val getSize: () -> IntSize,
    val getLastBitMap:  () -> ImageBitmap,
    val getCurrentBitMap:  () -> ImageBitmap,
    val getNextBitMap:  () -> ImageBitmap
) {

    //不让x,y为0,否则在点计算时会有问题
    private var mTouchX = 0.1f
    private var mTouchY = 0.1f

    // 拖拽点对应的页脚
    private var mCornerX = 1
    private var mCornerY = 1
    private val mPath0: Path = Path()
    private val mPath1: Path = Path()

    // 贝塞尔曲线起始点
    private val mBezierStart1 = PointF()

    // 贝塞尔曲线控制点
    private val mBezierControl1 = PointF()

    // 贝塞尔曲线顶点
    private val mBezierVertex1 = PointF()

    // 贝塞尔曲线结束点
    private var mBezierEnd1 = PointF()

    // 另一条贝塞尔曲线
    // 贝塞尔曲线起始点
    private val mBezierStart2 = PointF()

    // 贝塞尔曲线控制点
    private val mBezierControl2 = PointF()

    // 贝塞尔曲线顶点
    private val mBezierVertex2 = PointF()

    // 贝塞尔曲线结束点
    private var mBezierEnd2 = PointF()

    private var mMiddleX = 0f
    private var mMiddleY = 0f
    private var mDegrees = 0f
    private var mTouchToCornerDis = 0f
    private var mColorMatrixFilter = ColorMatrixColorFilter(
        ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    )
    private val mMatrix: Matrix = Matrix()
    private val mMatrixArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f)

    // 是否属于右上左下
    private var mIsRtOrLb = false

    // 背面颜色组
    // private var mBackShadowColors: IntArray
    //
    // // 前面颜色组
    // private var mFrontShadowColors: IntArray
    //
    // // 有阴影的GradientDrawable
    private var mBackShadowDrawableLR: Brush
    private var mBackShadowDrawableRL: Brush
    private var mFolderShadowDrawableLR: Brush
    private var mFolderShadowDrawableRL: Brush

    private var mFrontShadowDrawableHBT: Brush
    private var mFrontShadowDrawableHTB: Brush
    private var mFrontShadowDrawableVLR: Brush
    private var mFrontShadowDrawableVRL: Brush

    private val mPaint: Paint = Paint().apply { style = PaintingStyle.Fill }
    protected var curBitmap: ImageBitmap? = null
    protected var prevBitmap: ImageBitmap? = null
    protected var nextBitmap: ImageBitmap? = null

    init {
        //设置颜色数组
        // val color = intArrayOf(0x333333, -0x4fcccccd)
        // val color = intArrayOf(Color.parseColor("#33000000"),Color.TRANSPRENT)
        // mFolderShadowDrawableRL = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, color)
        mFolderShadowDrawableRL = Brush.linearGradient(0f to Color.Transparent, 1.0f to Color(0x33000000), start = Offset.Infinite, end = Offset.Zero)
        // mFolderShadowDrawableRL.gradientType = GradientDrawable.LINEAR_GRADIENT

        mFolderShadowDrawableLR = Brush.linearGradient(0f to Color.Transparent, 1.0f to Color(0x33000000))
        // mFolderShadowDrawableLR.gradientType = GradientDrawable.LINEAR_GRADIENT

        // mBackShadowColors = intArrayOf(-0xeeeeef, 0x111111)
        // mBackShadowColors = intArrayOf(Color.parseColor("#33000000"), Color.TRANSPARENT)

        mBackShadowDrawableRL = Brush.linearGradient(0f to Color.Transparent, 1.0f to Color(0x33000000), start = Offset.Infinite, end = Offset.Zero)
        // GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, mBackShadowColors)
        // mBackShadowDrawableRL.gradientType = GradientDrawable.LINEAR_GRADIENT

        mBackShadowDrawableLR = Brush.linearGradient(0f to Color.Transparent, 1.0f to Color(0x33000000))

        // mFrontShadowColors = intArrayOf(-0x7feeeeef, 0x111111)
        // mFrontShadowColors = intArrayOf(Color.parseColor("#33000000"), Color.TRANSPARENT)
        mFrontShadowDrawableVLR = Brush.linearGradient(0f to Color.Transparent, 1.0f to Color(0x33000000))
        // GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, mFrontShadowColors)
        // mFrontShadowDrawableVLR.gradientType = GradientDrawable.LINEAR_GRADIENT

        mFrontShadowDrawableVRL =
            Brush.linearGradient(0f to Color.Transparent, 1.0f to Color(0x33000000), start = Offset.Infinite, end = Offset.Zero)
        // mFrontShadowDrawableVRL.gradientType = GradientDrawable.LINEAR_GRADIENT

        mFrontShadowDrawableHTB = Brush.linearGradient(
            0f to Color.Transparent, 1.0f to Color(0x33000000),
            start = Offset.Infinite,
            end = Offset.Zero
        )
        // GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, mFrontShadowColors)
        // mFrontShadowDrawableHTB.gradientType = GradientDrawable.LINEAR_GRADIENT

        mFrontShadowDrawableHBT = Brush.linearGradient(
            0f to Color.Transparent,
            1.0f to Color(0x33000000),
            start = Offset.Zero,
            end = Offset.Infinite
        )
        // GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, mFrontShadowColors)
        // mFrontShadowDrawableHBT.gradientType = GradientDrawable.LINEAR_GRADIENT
    }

    fun onDestroy() {
        prevBitmap = null
        curBitmap = null
        nextBitmap = null
    }

    val viewWidth: Int
        get() {
            return getSize().width
        }
    val viewHeight get() = getSize().height
    val prevPage: ImageBitmap
        get() {
            return getLastBitMap()
            // val lastIndex = horizonReadWidget.currentItem - 1
            // val view = horizonReadWidget.layoutManager?.findViewByPosition(lastIndex)
            // return view ?: View(horizonReadWidget.context)
        }

    val curPage: ImageBitmap
        get() {
            return getCurrentBitMap()
        }
    val nextPage: ImageBitmap
        get() {
            return getNextBitMap()
        }
    val mMaxLength: Float
        get() = hypot(viewWidth.toDouble(), viewHeight.toDouble()).toFloat()

    var mDirection = PageDirection.NONE
    private fun setBitmap() {
        when (mDirection) {
            PageDirection.PREV -> {
//                prevBitmap = prevPage.screenshot()  //createBitMap1(prevPage)
//                curBitmap = curPage.screenshot()   //createBitMap2(curPage)
                prevBitmap = prevPage
                curBitmap = curPage
            }

            PageDirection.NEXT -> {
//                nextBitmap = nextPage.screenshot() // createBitMap1(nextPage)
//                curBitmap = curPage.screenshot() //createBitMap2(curPage)
                nextBitmap = nextPage
                curBitmap = curPage
            }

            else -> Unit
        }
    }

    var isMoved = false
    var isRunning = false
    var isStarted = false
    var touchX = 0f
    var touchY = 0f
    var isCancel = false

    var scaledTouchSlop = 10
    private fun onScroll(event: MotionEvent) {
        val action: Int = event.action
        // val pointerUp =
        //     action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_POINTER_UP
        // val skipIndex = if (pointerUp) event.actionIndex else -1
        // Determine focal point
        var sumX = event.x
        var sumY = event.y
        // val count: Int = event.pointerCount
        // for (i in 0 until count) {
        //     if (skipIndex == i) continue
        //     sumX += event.getX(i)
        //     sumY += event.getY(i)
        // }
        // val div = if (pointerUp) count - 1 else count
        // val focusX = sumX / div
        // val focusY = sumY / div

        val focusX = sumX
        val focusY = sumY
        //判断是否移动了
        if (!isMoved) {
            val deltaX = (focusX - startX).toInt()
            val deltaY = (focusY - startY).toInt()
            val distance = deltaX * deltaX + deltaY * deltaY
            isMoved = distance > scaledTouchSlop
            if (isMoved) {
                if (sumX - startX > 0) {
                    //如果上一页不存在
                    if (!hasPrev()) {
                        // noNext = true
                        return
                    }
                    setDirection(PageDirection.PREV)
                } else {
                    //如果不存在表示没有下一页了
                    if (!hasNext()) {
                        return
                    }
                    setDirection(PageDirection.NEXT)
                }
            }
        }
        if (isMoved) {
            isCancel = if (mDirection == PageDirection.NEXT) sumX > startX else sumX < startX
            isRunning = true
            //设置触摸点
            touchX = sumX
            touchY = sumY
            // horizonReadWidget.invalidate()
        }
    }

    private fun hasNext(): Boolean {
        return true
        // return horizonReadWidget.currentItem < ((horizonReadWidget.adapter?.itemCount ?: 0) - 1)
    }

    private fun hasPrev(): Boolean {
        return false
        // return horizonReadWidget.currentItem > 0
    }

    fun abortAnim() {
        isStarted = false
        isMoved = false
        isRunning = false
        if (!scroller.isFinished()) {
            scroller.abortAnimation()
            if (!isCancel) {
                fillPage(mDirection)
                invalidate()
                // reset()
            }
        } else {
            // readView.isAbortAnim = false
        }
    }

    fun invalidate() {
        // horizonReadWidget.invalidate()
    }

    fun nextPageByAnim(): Boolean {
        abortAnim()
        if (!hasNext()) return false
        setDirection(PageDirection.NEXT)
        val y = when {
            viewHeight / 2 < startY -> viewHeight.toFloat() * 0.9f
            else -> 1f
        }
        startX = viewWidth.toFloat() * 0.9f
        startY = y
        touchX = startX
        touchY = startY
        onAnimStart(300)
        return true
    }

    fun prevPageByAnim() {
        abortAnim()
        if (!hasPrev()) return
        setDirection(PageDirection.PREV)
        startX = 0f
        startY = viewHeight.toFloat()
        touchX = startX
        touchY = startY
        onAnimStart(300)
    }

    private fun fillPage(mDirection: PageDirection) {
        when (mDirection) {
            PageDirection.NEXT -> {
                // val nextPosition = horizonReadWidget.currentItem + 1
                // Log.d(TAG, "fillPage nextPosition $nextPosition")
                // horizonReadWidget.scrollTo(horizonReadWidget.currentItem + 1, false)
            }

            PageDirection.PREV -> {
                // horizonReadWidget.scrollTo(horizonReadWidget.currentItem - 1, false)
            }

            PageDirection.NONE -> {
            }
        }
    }

    fun onDown() {
        //是否移动
        isMoved = false
        //是否存在下一章
        // noNext = false
        //是否正在执行动画
        isRunning = false
        //取消
        canDisPatchTouchMove = false
        isCancel = false
        //是下一章还是前一章
        setDirection(PageDirection.NONE)
    }

    var startX = 0f
    var startY = 0f
    var canDisPatchTouchMove = false
    val slopSquare = 10

    val modifier = Modifier.pointerInput(Unit) {
        var startx = 0f
        var starty = 0f
        detectDragGestures(onDragStart = { event ->
            startx = event.x
            starty = event.y
            onTouch(MotionEvent(MotionEvent.ACTION_DOWN, event.x, event.y))
        }, onDragCancel = {
            onTouch(MotionEvent(MotionEvent.ACTION_CANCEL, startx, starty))
        }, onDragEnd = {
            onTouch(MotionEvent(MotionEvent.ACTION_CANCEL, startx, starty))
        }
        ) { change: PointerInputChange, dragAmount: Offset ->
            startx += dragAmount.x
            starty += dragAmount.x
            onTouch(MotionEvent(MotionEvent.ACTION_MOVE, startx, starty))
        }
    }.drawWithContent {
        drawContent()
        onDraw(this)
    }.graphicsLayer {

    }

    fun onTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                abortAnim()
                onDown()
                calcCornerXY(event.x, event.y)
                startX = event.x
                startY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                if (!canDisPatchTouchMove) {
                    canDisPatchTouchMove =
                        abs(startX - event.x) > slopSquare || abs(startY - event.y) > slopSquare
                }
                if (canDisPatchTouchMove) {
                    onScroll(event)
                }

                if ((startY > viewHeight / 3 && startY < viewHeight * 2 / 3)
                    || mDirection == PageDirection.PREV
                ) {
                    touchY = viewHeight.toFloat()
                }

                if (startY > viewHeight / 3 && startY < viewHeight / 2
                    && mDirection == PageDirection.NEXT
                ) {
                    touchY = 1f
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                onAnimStart(300)
            }
        }
        return isMoved
    }

    fun setDirection(direction: PageDirection) {
        mDirection = direction
        setBitmap()
        when (direction) {
            PageDirection.PREV ->
                //上一页滑动不出现对角
                if (startX > viewWidth / 2) {
                    calcCornerXY(startX, viewHeight.toFloat())
                } else {
                    calcCornerXY(viewWidth - startX, viewHeight.toFloat())
                }

            PageDirection.NEXT ->
                if (viewWidth / 2 > startX) {
                    calcCornerXY(viewWidth - startX, startY)
                }

            else -> Unit
        }
    }

    fun onAnimStart(animationSpeed: Int) {
        var dx: Float
        val dy: Float
        // dy 垂直方向滑动的距离，负值会使滚动向上滚动
        if (isCancel) {
            dx = if (mCornerX > 0 && mDirection == PageDirection.NEXT) {
                (viewWidth - touchX)
            } else {
                -touchX
            }
            if (mDirection != PageDirection.NEXT) {
                dx = -(viewWidth + touchX)
            }
            dy = if (mCornerY > 0) {
                (viewHeight - touchY)
            } else {
                -touchY // 防止mTouchY最终变为0
            }
        } else {
            dx = if (mCornerX > 0 && mDirection == PageDirection.NEXT) {
                -(viewWidth + touchX)
            } else {
                (viewWidth - touchX)
            }
            dy = if (mCornerY > 0) {
                (viewHeight - touchY)
            } else {
                (1 - touchY) // 防止mTouchY最终变为0
            }
        }
        startScroll(touchX.toInt(), touchY.toInt(), dx.toInt(), dy.toInt(), animationSpeed)
    }

    protected val scroller: Scroller by lazy {
        Scroller(LinearInterpolator())
    }

    protected fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, animationSpeed: Int) {
        val duration = if (dx != 0) {
            (animationSpeed * abs(dx)) / viewWidth
        } else {
            (animationSpeed * abs(dy)) / viewHeight
        }
        scroller.startScroll(startX, startY, dx, dy, duration)
        isRunning = true
        isStarted = true
        // horizonReadWidget.invalidate()
    }

    fun scroll() {
        if (scroller.computeScrollOffset()) {
            touchX = scroller.currX.toFloat()
            touchY = scroller.currY.toFloat()
            // horizonReadWidget.invalidate()
        } else if (isStarted) {
            onAnimStop()
            stopScroll()
        }
    }

    protected fun stopScroll() {
        isStarted = false
        // horizonReadWidget.post {
        //     isMoved = false
        //     isRunning = false
        //     horizonReadWidget.invalidate()
        // }
    }

    fun onAnimStop() {
        if (!isCancel) {
            fillPage(mDirection)
            // readView.fillPage(mDirection)
        }
    }

    private fun onDraw(canvas: ContentDrawScope) {
        if (!isRunning) return
        when (mDirection) {
            PageDirection.NEXT -> {
                calcPoints()
                drawCurrentPageArea(canvas, curBitmap)
                drawNextPageAreaAndShadow(canvas, nextBitmap)
                drawCurrentPageShadow(canvas)
                drawCurrentBackArea(canvas, curBitmap)
            }

            PageDirection.PREV -> {
                calcPoints()
                drawCurrentPageArea(canvas, prevBitmap)
                drawNextPageAreaAndShadow(canvas, curBitmap)
                drawCurrentPageShadow(canvas)
                drawCurrentBackArea(canvas, prevBitmap)
            }

            else -> return
        }
    }

    /**
     * 绘制翻起页背面
     */
    private fun drawCurrentBackArea(
        canvas: DrawScope,
        bitmap: ImageBitmap?
    ) {
        bitmap ?: return
        val i = ((mBezierStart1.x + mBezierControl1.x) / 2).toInt()
        val f1 = abs(i - mBezierControl1.x)
        val i1 = ((mBezierStart2.y + mBezierControl2.y) / 2).toInt()
        val f2 = abs(i1 - mBezierControl2.y)
        val f3 = min(f1, f2)
        mPath1.reset()
        mPath1.moveTo(mBezierVertex2.x, mBezierVertex2.y)
        mPath1.lineTo(mBezierVertex1.x, mBezierVertex1.y)
        mPath1.lineTo(mBezierEnd1.x, mBezierEnd1.y)
        mPath1.lineTo(mTouchX, mTouchY)
        mPath1.lineTo(mBezierEnd2.x, mBezierEnd2.y)
        mPath1.close()
        val mFolderShadowDrawable: Brush
        val left: Int
        val right: Int
        if (mIsRtOrLb) {
            left = (mBezierStart1.x - 1).toInt()
            right = (mBezierStart1.x + f3 + 1).toInt()
            mFolderShadowDrawable = mFolderShadowDrawableLR
        } else {
            left = (mBezierStart1.x - f3 - 1).toInt()
            right = (mBezierStart1.x + 1).toInt()
            mFolderShadowDrawable = mFolderShadowDrawableRL
        }
        canvas.withTransform({}) {
            clipPath(mPath0) {
                clipPath(mPath1) {

                    mPaint.colorFilter = mColorMatrixFilter
                    val dis = hypot(
                        mCornerX - mBezierControl1.x.toDouble(),
                        mBezierControl2.y - mCornerY.toDouble()
                    ).toFloat()
                    val f8 = (mCornerX - mBezierControl1.x) / dis
                    val f9 = (mBezierControl2.y - mCornerY) / dis
                    mMatrixArray[0] = 1 - 2 * f9 * f9
                    mMatrixArray[1] = 2 * f8 * f9
                    mMatrixArray[3] = mMatrixArray[1]
                    mMatrixArray[4] = 1 - 2 * f8 * f8
                    mMatrix.reset()
                    mMatrix.setValues(mMatrixArray)
                    mMatrix.preTranslate(-mBezierControl1.x, -mBezierControl1.y)
                    mMatrix.translate(mBezierControl1.x, mBezierControl1.y)

                    canvas.drawBitmap(bitmap, mMatrix, mColorMatrixFilter)
                    mPaint.colorFilter = null
                    canvas.withTransform({
                        rotate(mDegrees, Offset(mBezierStart1.x, mBezierStart1.y))
                    }, {
                        drawRect(
                            mFolderShadowDrawable,
                            topLeft = Offset(left.toFloat(), mBezierStart1.y),
                            size = Size(
                                right.toFloat() - left.toFloat(),
                                (mBezierStart1.y + mMaxLength) - mBezierStart1.y
                            )
                        )
                        // mFolderShadowDrawable.setBounds(
                        //     left, mBezierStart1.y.toInt(),
                        //     right, (mBezierStart1.y + mMaxLength).toInt()
                        // )
                        // mFolderShadowDrawable.draw(canvas)
                    })

                }
            }
        }
    }

    fun DrawScope.drawBitmap(
        bitmap: ImageBitmap,
        matrix: Matrix = Matrix(),
        paint: ColorFilter? = null,
    ) {
        withTransform({
            transform(matrix)
        }) {
            drawImage(
                image = bitmap,
                topLeft = Offset.Zero,
                colorFilter = paint
            )
        }
    }

    fun Matrix.preTranslate(tx: Float, ty: Float) {
        val original = this.values
        this.values[0] = original[0]
        this.values[1] = original[1]
        this.values[2] = original[0] * tx + original[1] * ty + original[2]
        this.values[3] = original[3]
        this.values[4] = original[4]
        this.values[5] = original[3] * tx + original[4] * ty + original[5]
        this.values[6] = original[6]
        this.values[7] = original[7]
        this.values[8] = original[6] * tx + original[7] * ty + original[8]
    }

    fun Matrix.setValues(original: FloatArray) {
        this.values[0] = original[0]
        this.values[1] = original[1]
        this.values[2] = original[2]
        this.values[3] = original[3]
        this.values[4] = original[4]
        this.values[5] = original[5]
        this.values[6] = original[6]
        this.values[7] = original[7]
        this.values[8] = original[8]
    }

    /**
     * 绘制翻起页的阴影
     */
    private fun drawCurrentPageShadow(canvas: DrawScope) {
        val degree: Double = if (mIsRtOrLb) {
            Math.PI / 4 - atan2(mBezierControl1.y - mTouchY, mTouchX - mBezierControl1.x)
        } else {
            Math.PI / 4 - atan2(mTouchY - mBezierControl1.y, mTouchX - mBezierControl1.x)
        }
        // 翻起页阴影顶点与touch点的距离
        val d1 = 25.toFloat() * 1.414 * cos(degree)
        val d2 = 25.toFloat() * 1.414 * sin(degree)
        val x = (mTouchX + d1).toFloat()
        val y: Float = if (mIsRtOrLb) {
            (mTouchY + d2).toFloat()
        } else {
            (mTouchY - d2).toFloat()
        }
        mPath1.reset()
        mPath1.moveTo(x, y)
        mPath1.lineTo(mTouchX, mTouchY)
        mPath1.lineTo(mBezierControl1.x, mBezierControl1.y)
        mPath1.lineTo(mBezierStart1.x, mBezierStart1.y)
        mPath1.close()
        var leftX: Int
        var rightX: Int
        var mCurrentPageShadow: Brush
        var rotateDegrees = Math.toDegrees(
            atan2(mTouchX - mBezierControl1.x, mBezierControl1.y - mTouchY).toDouble()
        ).toFloat()
        canvas.withTransform({}, {
            clipPath(mPath0) {
                if (mIsRtOrLb) {
                    leftX = mBezierControl1.x.toInt()
                    rightX = (mBezierControl1.x + 25).toInt()
                    mCurrentPageShadow = mFrontShadowDrawableVLR
                } else {
                    leftX = (mBezierControl1.x - 25).toInt()
                    rightX = (mBezierControl1.x + 1).toInt()
                    mCurrentPageShadow = mFrontShadowDrawableVRL
                }

                rotate(rotateDegrees, Offset(mBezierControl1.x, mBezierControl1.y)) {
                    drawRect(
                        brush = mCurrentPageShadow,
                        topLeft = Offset(leftX.toFloat(), (mBezierControl1.y - mMaxLength)),
                        size = Size(rightX - leftX.toFloat(), mBezierControl1.y - (mBezierControl1.y - mMaxLength))
                    )
                }
                // mCurrentPageShadow.setBounds(
                //     leftX, (mBezierControl1.y - mMaxLength).toInt(),
                //     rightX, mBezierControl1.y.toInt()
                // )
                // mCurrentPageShadow.draw(canvas)
            }

        })


        canvas.withTransform({}) {
            mPath1.reset()
            mPath1.moveTo(x, y)
            mPath1.lineTo(mTouchX, mTouchY)
            mPath1.lineTo(mBezierControl2.x, mBezierControl2.y)
            mPath1.lineTo(mBezierStart2.x, mBezierStart2.y)
            mPath1.close()
            clipPath(mPath0, ClipOp.Difference) {

                clipPath(mPath1) {

                    if (mIsRtOrLb) {
                        leftX = mBezierControl2.y.toInt()
                        rightX = (mBezierControl2.y + 25).toInt()
                        mCurrentPageShadow = mFrontShadowDrawableHTB
                    } else {
                        leftX = (mBezierControl2.y - 25).toInt()
                        rightX = (mBezierControl2.y + 1).toInt()
                        mCurrentPageShadow = mFrontShadowDrawableHBT
                    }
                    rotateDegrees = Math.toDegrees(
                        atan2(mBezierControl2.y - mTouchY, mBezierControl2.x - mTouchX).toDouble()
                    ).toFloat()
                    rotate(rotateDegrees, Offset(mBezierControl2.x, mBezierControl2.y)) {
                        val temp =
                            if (mBezierControl2.y < 0) (mBezierControl2.y - viewHeight).toDouble()
                            else mBezierControl2.y.toDouble()
                        val hmg = hypot(mBezierControl2.x.toDouble(), temp)
                        if (hmg > mMaxLength) {
                            val left = (mBezierControl2.x - 25 - hmg).toFloat()
                            val top = left
                            val width = (mBezierControl2.x + mMaxLength - hmg).toInt() - left
                            val height = rightX - top
                            drawRect(mCurrentPageShadow, topLeft = Offset(left,top), size = Size(width, height))
                            // mCurrentPageShadow.setBounds(
                            //     (mBezierControl2.x - 25 - hmg).toInt(), leftX,
                            //     (mBezierControl2.x + mMaxLength - hmg).toInt(), rightX
                            // )
                        }
                        else {

                            val left =  (mBezierControl2.x - mMaxLength).toFloat()
                            val top = leftX.toFloat()
                            val width =  mBezierControl2.x.toInt() - left
                            val height = rightX - top
                            drawRect(mCurrentPageShadow, topLeft = Offset(left,top), size = Size(width, height))
                            // mCurrentPageShadow.setBounds(
                            //     (mBezierControl2.x - mMaxLength).toInt(), leftX,
                            //     mBezierControl2.x.toInt(), rightX
                            // )
                            // mCurrentPageShadow.draw(canvas)
                        }
                    }

                }
            }

        }
    }

    //
    private fun drawNextPageAreaAndShadow(
        canvas: DrawScope,
        bitmap: ImageBitmap?
    ) {
        bitmap ?: return
        mPath1.reset()
        mPath1.moveTo(mBezierStart1.x, mBezierStart1.y)
        mPath1.lineTo(mBezierVertex1.x, mBezierVertex1.y)
        mPath1.lineTo(mBezierVertex2.x, mBezierVertex2.y)
        mPath1.lineTo(mBezierStart2.x, mBezierStart2.y)
        mPath1.lineTo(mCornerX.toFloat(), mCornerY.toFloat())
        mPath1.close()
        mDegrees = Math.toDegrees(
            atan2(
                (mBezierControl1.x - mCornerX).toDouble(),
                mBezierControl2.y - mCornerY.toDouble()
            )
        ).toFloat()
        val leftX: Int
        val rightX: Int
        val mBackShadowDrawable: Brush
        if (mIsRtOrLb) { //左下及右上
            leftX = mBezierStart1.x.toInt()
            rightX = (mBezierStart1.x + mTouchToCornerDis / 4).toInt()
            mBackShadowDrawable = mBackShadowDrawableLR
        } else {
            leftX = (mBezierStart1.x - mTouchToCornerDis / 4).toInt()
            rightX = mBezierStart1.x.toInt()
            mBackShadowDrawable = mBackShadowDrawableRL
        }
        canvas.withTransform({
            clipPath(mPath0)
            clipPath(mPath1)
        },{
            drawBitmap(bitmap)
            rotate(mDegrees, Offset( mBezierStart1.x, mBezierStart1.y)){
                val offSet = Offset(leftX.toFloat(),mBezierStart1.y)
                val size = Size(rightX-offSet.x,mMaxLength + mBezierStart1.y-offSet.y)
                drawRect(mBackShadowDrawable, topLeft = offSet, size = size)
                // mBackShadowDrawable.setBounds(
                //     leftX, mBezierStart1.y.toInt(),
                //     rightX, (mMaxLength + mBezierStart1.y).toInt()
                // ) //左上及右下角的xy坐标值,构成一个矩形
                // mBackShadowDrawable.draw(canvas)
                // canvas.restore()
            }
        })


    }

    //
    private fun drawCurrentPageArea(
        canvas: ContentDrawScope,
        bitmap: ImageBitmap?
    ) {
        bitmap ?: return
        mPath0.reset()
        mPath0.moveTo(mBezierStart1.x, mBezierStart1.y)
        mPath0.quadraticTo(mBezierControl1.x, mBezierControl1.y, mBezierEnd1.x, mBezierEnd1.y)
        mPath0.lineTo(mTouchX, mTouchY)
        mPath0.lineTo(mBezierEnd2.x, mBezierEnd2.y)
        mPath0.quadraticTo(mBezierControl2.x, mBezierControl2.y, mBezierStart2.x, mBezierStart2.y)
        mPath0.lineTo(mCornerX.toFloat(), mCornerY.toFloat())
        mPath0.close()
        canvas.withTransform({}) {
            clipPath(mPath0) {
                drawImage(bitmap)
            }
        }
    }

    /**
     * 计算拖拽点对应的拖拽脚
     */
    private fun calcCornerXY(x: Float, y: Float) {
        mCornerX = if (x <= viewWidth / 2) 0 else viewWidth
        mCornerY = if (y <= viewHeight / 2) 0 else viewHeight
        mIsRtOrLb = (mCornerX == 0 && mCornerY == viewHeight)
            || (mCornerY == 0 && mCornerX == viewWidth)

        // Log.d(TAG, "mCornerX $mCornerX + mCornerY $mCornerY mIsRtOrLb $mIsRtOrLb")
    }

    private fun calcPoints() {
        mTouchX = touchX
        mTouchY = touchY

        mMiddleX = (mTouchX + mCornerX) / 2
        mMiddleY = (mTouchY + mCornerY) / 2
        mBezierControl1.x =
            mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX)
        mBezierControl1.y = mCornerY.toFloat()
        mBezierControl2.x = mCornerX.toFloat()

        val f4 = mCornerY - mMiddleY
        if (f4 == 0f) {
            mBezierControl2.y = mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / 0.1f
        } else {
            mBezierControl2.y =
                mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY)
        }
        mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2
        mBezierStart1.y = mCornerY.toFloat()

        // 固定左边上下两个点
        if (mTouchX > 0 && mTouchX < viewWidth) {
            if (mBezierStart1.x < 0 || mBezierStart1.x > viewWidth) {
                if (mBezierStart1.x < 0)
                    mBezierStart1.x = viewWidth - mBezierStart1.x

                val f1 = abs(mCornerX - mTouchX)
                val f2 = viewWidth * f1 / mBezierStart1.x
                mTouchX = abs(mCornerX - f2)

                val f3 = abs(mCornerX - mTouchX) * abs(mCornerY - mTouchY) / f1
                mTouchY = abs(mCornerY - f3)

                mMiddleX = (mTouchX + mCornerX) / 2
                mMiddleY = (mTouchY + mCornerY) / 2

                mBezierControl1.x =
                    mMiddleX - (mCornerY - mMiddleY) * (mCornerY - mMiddleY) / (mCornerX - mMiddleX)
                mBezierControl1.y = mCornerY.toFloat()

                mBezierControl2.x = mCornerX.toFloat()

                val f5 = mCornerY - mMiddleY
                if (f5 == 0f) {
                    mBezierControl2.y =
                        mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / 0.1f
                } else {
                    mBezierControl2.y =
                        mMiddleY - (mCornerX - mMiddleX) * (mCornerX - mMiddleX) / (mCornerY - mMiddleY)
                }

                mBezierStart1.x = mBezierControl1.x - (mCornerX - mBezierControl1.x) / 2
            }
        }
        mBezierStart2.x = mCornerX.toFloat()
        mBezierStart2.y = mBezierControl2.y - (mCornerY - mBezierControl2.y) / 2

        mTouchToCornerDis = hypot(
            (mTouchX - mCornerX).toDouble(),
            (mTouchY - mCornerY).toDouble()
        ).toFloat()

        mBezierEnd1 = getCross(
            PointF(mTouchX, mTouchY), mBezierControl1, mBezierStart1,
            mBezierStart2
        )
        mBezierEnd2 = getCross(
            PointF(mTouchX, mTouchY), mBezierControl2, mBezierStart1,
            mBezierStart2
        )

        mBezierVertex1.x = (mBezierStart1.x + 2 * mBezierControl1.x + mBezierEnd1.x) / 4
        mBezierVertex1.y = (2 * mBezierControl1.y + mBezierStart1.y + mBezierEnd1.y) / 4
        mBezierVertex2.x = (mBezierStart2.x + 2 * mBezierControl2.x + mBezierEnd2.x) / 4
        mBezierVertex2.y = (2 * mBezierControl2.y + mBezierStart2.y + mBezierEnd2.y) / 4
    }

    /**
     * 求解直线P1P2和直线P3P4的交点坐标
     */
    private fun getCross(P1: PointF, P2: PointF, P3: PointF, P4: PointF): PointF {
        val crossP = PointF()
        // 二元函数通式： y=ax+b
        val a1 = (P2.y - P1.y) / (P2.x - P1.x)
        val b1 = (P1.x * P2.y - P2.x * P1.y) / (P1.x - P2.x)
        val a2 = (P4.y - P3.y) / (P4.x - P3.x)
        val b2 = (P3.x * P4.y - P4.x * P3.y) / (P3.x - P4.x)
        crossP.x = (b2 - b1) / (a1 - a2)
        crossP.y = a1 * crossP.x + b1
        return crossP
    }

    companion object {
        private const val TAG = "SimulateEffect"
    }
}

enum class PageDirection {
    NEXT, PREV, NONE
}