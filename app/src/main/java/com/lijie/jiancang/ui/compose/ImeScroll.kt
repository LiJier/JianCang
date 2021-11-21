package com.lijie.jiancang.ui.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat


var selectionTop by mutableStateOf(0)
private var isVisible by mutableStateOf(false)
private var imeBottom by mutableStateOf(0)
private var maxImeBottom by mutableStateOf(0)

fun Modifier.imeScroll(height: Int, scrollState: ScrollState) = composed(
    factory = {
        val view = LocalView.current
        DisposableEffect(view) {
            ViewCompat.setWindowInsetsAnimationCallback(view, object :
                WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {

                override fun onProgress(
                    wic: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>
                ): WindowInsetsCompat {
                    imeBottom = wic.getInsets(WindowInsetsCompat.Type.ime()).bottom
                    return wic
                }
            })
            onDispose {
                selectionTop = 0
                isVisible = false
                imeBottom = 0
                maxImeBottom = 0
            }
        }
        if (maxImeBottom < imeBottom) {
            maxImeBottom = imeBottom
        }
        val scrollValue = scrollState.value
        isVisible = imeBottom != 0
        if (isVisible) {
            if ((selectionTop - scrollValue) > maxImeBottom) {
                val to = (maxImeBottom - (height - (selectionTop - scrollValue))) + 200
                if (to > 0) {
                    LaunchedEffect(imeBottom) {
                        scrollState.scrollTo(
                            (scrollState.value + to)
                        )
                    }
                }
            }
        }
        verticalScroll(scrollState)
    }
)

fun Modifier.imeHeight() = composed(
    factory = {
        val imeBottomDb = with(LocalDensity.current) { imeBottom.toDp() }
        this.height(imeBottomDb)
    }
)