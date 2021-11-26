package com.lijie.jiancang.ui.compose

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.viewinterop.AndroidView
import com.lijie.jiancang.ext.article

@ExperimentalUnitApi
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(
    url: String,
    modifier: Modifier = Modifier,
    onLoadComplete: ((String, String) -> Unit)? = null
) {

    class InJavaScriptLocalObj {

        @JavascriptInterface
        fun htmlSource(html: String) {
            val (title, newHtml) = html.article()
            onLoadComplete?.invoke(title, newHtml)
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
                    "javascript:window.local_obj.htmlSource(document.getElementsByTagName('html')[0].innerHTML);"
                )
                super.onPageFinished(view, url)
            }

        }
        webView.loadUrl(url)
        webView
    }, modifier, update = {
        it.loadUrl(url)
    })

}