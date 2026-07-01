package com.cloudview.novel.readview.gesture.capture

import kotlin.time.Duration

/**
 * CloudView limited 2025 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2025/3/31
 * @Time: 20:03
 * @version: V1.0
 */
class Scroller(val linearInterpolator: LinearInterpolator) {

    fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
    }

    fun computeScrollOffset(): Boolean {
        return true
    }

    val currX: Int = 0

    val currY: Int = 0

    fun isFinished():Boolean = false

    fun abortAnimation(){}
}

class LinearInterpolator()

class MotionEvent(val action: Int, val x: Float, val y: Float) {
    companion object {
        val ACTION_DOWN = 1
        val ACTION_MOVE = 2
        val ACTION_CANCEL = 3
    }
}