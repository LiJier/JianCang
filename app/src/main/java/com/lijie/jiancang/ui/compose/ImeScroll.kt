package com.lijie.jiancang.ui.compose

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat

class SelectionTop(
    var value: Int
)

val LocalSelectionTop = staticCompositionLocalOf {
    SelectionTop(0)
}

fun Modifier.imeScroll(height: Int, scrollState: ScrollState) = composed(
    factory = {
        val top = LocalSelectionTop.current
        val view = LocalView.current
        var isVisible by remember { mutableStateOf(false) }
        var imeBottom by remember { mutableStateOf(0) }
        var maxImeBottom by remember { mutableStateOf(0) }
        ViewCompat.setOnApplyWindowInsetsListener(
            view,
            OnApplyWindowInsetsListener { _, wic ->
                isVisible = wic.isVisible(WindowInsetsCompat.Type.ime())
                return@OnApplyWindowInsetsListener wic
            })
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
        if (maxImeBottom < imeBottom) {
            maxImeBottom = imeBottom
        }
        val scrollValue = scrollState.value
        if (isVisible) {
            if ((top.value - scrollValue) > maxImeBottom) {
                val to = (maxImeBottom - (height - (top.value - scrollValue))) + 200
                if (to > 0) {
                    LaunchedEffect(imeBottom) {
                        scrollState.animateScrollTo(
                            (scrollState.value + to),
                            SpringSpec(stiffness = Spring.StiffnessHigh)
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
        val view = LocalView.current
        var imeBottom by remember { mutableStateOf(0) }
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
        val imeBottomDb = with(LocalDensity.current) { imeBottom.toDp() }
        this.height(imeBottomDb)
    }
)