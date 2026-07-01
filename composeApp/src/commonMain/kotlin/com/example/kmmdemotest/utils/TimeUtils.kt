package com.example.kmmdemotest.utils

/**
 * @author onuszhao
 * @since 2025/10/9
 * @description
 */

expect object TimeUtils {

    fun formatDate(timeInMillis: Long, format: String): String
}