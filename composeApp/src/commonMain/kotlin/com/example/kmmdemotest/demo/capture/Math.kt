package com.cloudview.novel.readview.gesture.capture

/**
 * CloudView limited 2025 copyright.
 *
 * @Description: TODO
 * @User: tonysheng
 * @Date: 2025/3/31
 * @Time: 19:40
 * @version: V1.0
 */
object Math {
    const val PI: Double = 3.14159265358979323846

    const val RADIANS_TO_DEGREES: Double = 57.29577951308232

    fun toDegrees(angrad: Double): Double {
        return angrad * RADIANS_TO_DEGREES
    }
}