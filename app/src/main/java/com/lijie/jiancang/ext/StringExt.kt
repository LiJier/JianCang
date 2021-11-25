package com.lijie.jiancang.ext

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.util.rangeTo
import com.lijie.jiancang.App
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

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

fun String.article(): Pair<String, String> {
    val doc = Jsoup.parse("<html>${this}</html>")
    val title = findTitle(doc)
    val article = findArticle(doc)
    return Pair(title, article)
}

private fun findTitle(doc: Document): String {
    var title = doc.title() ?: ""
    if (title.isEmpty()) {
        val elements = doc.head().getElementsByTag("meta")
        val titleEle = elements.filter {
            it.attr("property").contains("title")
        }
        title = titleEle.getOrNull(0)?.attr("content") ?: ""
    }
    val hText = arrayListOf<String>()
    doc.allElements.forEach {
        val tagName = it.tagName()
        if (tagName == "h1" || tagName == "h2" || tagName == "h3" || tagName == "h4" || tagName == "h5") {
            if (it.childrenSize() == 0 && it.hasText()) {
                hText.add(it.text())
            }
        }
    }
    hText.forEach {
        if (title.isEmpty()) {
            title = it
        } else {
            if (it.contains(title)) {
                title = it
            }
        }
    }
    return title
}

private fun findArticle(doc: Document): String {
    doc.getElementsByTag("script").remove()
    var targetElement = doc.getElementsByTag("article")
    if (targetElement.size == 0) {
        targetElement = doc.body().allElements
    }
    val sb = StringBuilder()
    var lastElement = Element("a")
    targetElement.forEach { element ->
        val elms = element.allElements
        if (elms.size <= 1) {
            val textNodes = elms.textNodes()
            if (textNodes.size > 0) {
                if (lastElement.allElements.contains(element).not()) {
                    if (element.hasText() || element.getElementsByTag("img").size > 0) {
                        if (visible(element)) {
                            sb.append(element.toString()).append("\n")
                            Log.d("article", element.toString())
                        }
                    }
                    lastElement = element
                }
            } else {
                if (elms.isEmpty()) {
                    if (lastElement.allElements.contains(element).not()) {
                        if (element.hasText() || element.getElementsByTag("img").size > 0) {
                            if (visible(element)) {
                                sb.append(element.toString()).append("\n")
                                Log.d("article", element.toString())
                            }
                        }
                        lastElement = element
                    }
                }
            }
        }
    }
    return sb.toString()
}

private fun visible(element: Element): Boolean {
    val text = element.text()
    val attr = StringBuilder()
    attr.append(element.attributes().toString())
    element.children().forEach {
        attr.append(it.tagName())
        attr.append(it.attributes().toString())
        attr.append(it.classNames().toString())
    }
    return (text.length > 10 ||
            (text.contains("！") ||
                    text.contains("，") ||
                    text.contains("。") ||
                    text.contains("？") ||
                    text.contains("、") ||
                    text.contains("；") ||
                    text.contains("：") ||
                    text.contains("“") ||
                    text.contains("”") ||
                    text.contains("‘") ||
                    text.contains("’") ||
                    text.contains("《") ||
                    text.contains("》") ||
                    text.contains("%") ||
                    text.contains("（") ||
                    text.contains("）") ||
                    text.contains(",") ||
                    text.contains(".") ||
                    text.contains("?") ||
                    text.contains(":") ||
                    text.contains(";") ||
                    text.contains("'") ||
                    text.contains("\"") ||
                    text.contains("!") ||
                    text.contains("%") ||
                    text.contains("(") ||
                    text.contains(")"))
            ) && ((attr.contains("hidden") ||
            attr.contains("hide") ||
            attr.contains("button") ||
            attr.contains("dialog") ||
            attr.contains("javascript") ||
            attr.contains("toast") ||
            attr.contains("btn") ||
            attr.contains("profile")).not())
}