package com.lijie.jiancang.ui.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import com.google.accompanist.insets.LocalWindowInsets


var selectionTop by mutableStateOf(0)

fun Modifier.imeScroll(height: Int, scrollState: ScrollState) = composed(
    factory = {
        val ime = LocalWindowInsets.current.ime
        val scrollValue = scrollState.value
        if (ime.isVisible) {
            if ((selectionTop - scrollValue) > ime.bottom) {
                val to = (ime.bottom - (height - (selectionTop - scrollValue))) + 200
                if (to > 0) {
                    LaunchedEffect(ime.bottom) {
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
        val ime = LocalWindowInsets.current.ime
        val imeBottomDb = with(LocalDensity.current) { ime.bottom.toDp() }
        this.height(imeBottomDb)
    }
)