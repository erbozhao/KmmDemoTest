package com.example.kmmdemotest.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.dateWithTimeIntervalSince1970

actual object TimeUtils {
    actual fun formatDate(timeInMillis: Long, format: String): String {
        val date = NSDate.dateWithTimeIntervalSince1970(timeInMillis.toDouble() / 1000.0)
        val dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = format
        dateFormatter.locale = NSLocale("en_US")
        return dateFormatter.stringFromDate(date)
    }
}