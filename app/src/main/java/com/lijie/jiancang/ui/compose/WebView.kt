package com.lijie.jiancang.ui.compose

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.viewinterop.AndroidView

@ExperimentalUnitApi
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(url: String) {
    val loadUrl by remember { mutableStateOf(url) }
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
}

class InJavaScriptLocalObj {

    @JavascriptInterface
    fun showSource(html: String) {

    }

}