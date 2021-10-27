package com.lijie.jiancang.ext

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.util.rangeTo
import com.lijie.jiancang.App
import java.util.regex.Pattern

var toast: Toast? = null

@SuppressLint("ShowToast")
fun String.toast() {
    toast?.let {
        it.setText(this)
        it.show()

    } ?: run {
        toast = Toast.makeText(App.appContext, this, Toast.LENGTH_SHORT)
        toast?.show()
    }
}

const val URL_TAG = "URL"
fun String.annotationUrl(checkIndex: Int = -1): AnnotatedString =
    buildAnnotatedString {
        val s = this@annotationUrl
        append(s)
        var endIndex = 0
        while (endIndex < s.length) {
            val startIndex = s.indexOf("http", endIndex)
            if (startIndex < 0) {
                endIndex = s.length
            } else {
                endIndex = s.indexOf(" ", startIndex, true)
                if (endIndex < 0) {
                    endIndex = s.length
                }
                addStyle(
                    style = SpanStyle(
                        color = if (checkIndex in (startIndex rangeTo endIndex)) Color(0xFF3F51B5) else Color(
                            0xff64B5F6
                        ),
                        background = if (checkIndex in (startIndex rangeTo endIndex)) Color(
                            0xff64B5F6
                        ) else Color(0x00000000),
                        textDecoration = TextDecoration.Underline
                    ), start = startIndex, end = endIndex
                )
                addStringAnnotation(
                    tag = URL_TAG,
                    annotation = s.substring(startIndex, endIndex),
                    start = startIndex,
                    end = endIndex
                )
            }
        }
    }

fun String.findUrl(): String? {
    val urlList = arrayListOf<String>()
    var endIndex = 0
    while (endIndex < length) {
        val startIndex = indexOf("http", endIndex)
        if (startIndex < 0) {
            endIndex = length
        } else {
            fun getEndIndex(string: String): Int {
                var i = indexOf(string, startIndex, true)
                if (i < 0) {
                    i = length
                }
                return i
            }
            endIndex =
                kotlin.math.min(getEndIndex(" "), getEndIndex(" "))
            if (endIndex < 0) {
                endIndex = length
            }
            urlList.add(substring(startIndex, endIndex))
        }
    }
    return urlList.lastOrNull()
}

fun String.filterHtml(): String {
    var str = this
    val regEx = "(?!<(img |a ).*?>)<.*?>"
    val pHtml = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE)
    val mHtml = pHtml.matcher(str)
    str = mHtml.replaceAll("")
    val pN = Pattern.compile("(\r?\n(\\s*\r?\n)+)")
    val pM = pN.matcher(str)
    str = pM.replaceAll("\r\n")
    return str.trim()
}