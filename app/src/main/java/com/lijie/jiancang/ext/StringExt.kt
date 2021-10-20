package com.lijie.jiancang.ext

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.lijie.jiancang.App

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

fun String.annotated(check: Boolean = false): AnnotatedString =
    buildAnnotatedString {
        val s = this@annotated
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
                        color = if (check) Color(0xFF3F51B5) else Color(0xff64B5F6),
                        background = if (check) Color(0xff64B5F6) else Color(0x00000000),
                        textDecoration = TextDecoration.Underline
                    ), start = startIndex, end = endIndex
                )
                addStringAnnotation(
                    tag = "URL",
                    annotation = s.substring(startIndex, endIndex),
                    start = startIndex,
                    end = endIndex
                )
            }
        }
    }