package com.example.kmmdemotest.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object TimeUtils {
    actual fun formatDate(timeInMillis: Long, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        return sdf.format(Date(timeInMillis))
    }
}