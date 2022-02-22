package com.lijie.jiancang.ui.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.google.accompanist.insets.LocalWindowInsets

var selectionTop by mutableStateOf(0)

@Composable
fun ScrollState.imeScroll(height: Int): ScrollState {
    val ime = LocalWindowInsets.current.ime
    val scrollValue = this.value
    if (ime.isVisible) {
        if ((selectionTop - scrollValue) > ime.bottom) {
            val to = (ime.bottom - (height - (selectionTop - scrollValue))) + 200
            if (to > 0) {
                LaunchedEffect(ime.bottom) {
                    this@imeScroll.scrollTo(
                        (this@imeScroll.value + to)
                    )
                }
            }
        }
    }
    return this
}

@Composable
fun imeHeight(): Dp {
    val ime = LocalWindowInsets.current.ime
    return with(LocalDensity.current) { ime.bottom.toDp() }
}