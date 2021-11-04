package com.lijie.jiancang.ui.compose

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.viewinterop.AndroidView
import com.overzealous.remark.Remark
import dev.jeziellago.compose.markdowntext.MarkdownText

@ExperimentalUnitApi
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(url: String) {

    val loadUrl by remember { mutableStateOf(url) }
    var mdContent by remember { mutableStateOf("") }

    class InJavaScriptLocalObj {

        @JavascriptInterface
        fun showSource(html: String) {
            md("<html>${html}</html>")
        }

        private fun md(html: String) {
            val convert = Remark().convert(html)
            mdContent = convert
            Log.d("WebView", convert)
        }

    }
    if (mdContent.isEmpty()) {
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
        })
    } else {
        MarkdownText(markdown = mdContent)
    }

}