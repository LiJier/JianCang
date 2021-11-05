package com.lijie.jiancang.ui.compose

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.viewinterop.AndroidView
import org.jsoup.Jsoup

@ExperimentalUnitApi
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(url: String, modifier: Modifier = Modifier, onLoadComplete: (String, String) -> Unit) {

    val loadUrl by remember { mutableStateOf(url) }

    class InJavaScriptLocalObj {

        @JavascriptInterface
        fun showSource(html: String) {
            val newHtml = "<html>${html}</html>"
            val jsoup = Jsoup.parse(newHtml)
            var title = jsoup.title()
            if (title.isNullOrEmpty()) {
                val elements = jsoup.head().getElementsByTag("meta")
                val titleEle = elements.filter {
                    it.attr("property").contains("title")
                }
                title = titleEle.getOrNull(0)?.attr("content") ?: ""
            }
            onLoadComplete(title, newHtml)
        }

    }
    AndroidView(factory = {
        val webView = WebView(it)
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.addJavascriptInterface(InJavaScriptLocalObj(), "local_obj")
        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                view.loadUrl(
                    "javascript:window.local_obj.showSource(document.getElementsByTagName('html')[0].innerHTML);"
                )
                super.onPageFinished(view, url)
            }

        }
        webView.loadUrl(loadUrl)
        webView
    }, modifier)

}