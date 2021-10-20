package com.lijie.jiancang.ext

import android.text.format.DateFormat

/**
 * 格式化时间
 * @param format 时间格式
 */
fun Long.toTime(format: String = "yyyy-MM-dd HH:mm:ss"): String =
    DateFormat.format(format, this).toString()