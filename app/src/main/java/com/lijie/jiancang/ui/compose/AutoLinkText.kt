package com.lijie.jiancang.ui.compose

import androidx.compose.foundation.gestures.*
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import com.lijie.jiancang.ext.URL_TAG
import com.lijie.jiancang.ext.annotationUrl
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex


@Composable
fun AutoLinkText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val uriHandler = LocalUriHandler.current
    var annotatedLinkString by remember { mutableStateOf(text.annotationUrl()) }
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val pressIndicator = Modifier.pointerInput(Unit) {
        detectPressAndTapOrUnconsumed(onPress = { pos ->
            layoutResult.value?.let { layoutResult ->
                val offset = layoutResult.getOffsetForPosition(pos)
                annotatedLinkString
                    .getStringAnnotations(URL_TAG, offset, offset)
                    .firstOrNull()?.let {
                        annotatedLinkString = text.annotationUrl(offset)
                        true
                    } ?: run {
                    false
                }
            } ?: run {
                false
            }
            tryAwaitRelease()
            annotatedLinkString = text.annotationUrl()
            false
        }) { pos ->
            layoutResult.value?.let { layoutResult ->
                val offset = layoutResult.getOffsetForPosition(pos)
                annotatedLinkString
                    .getStringAnnotations(URL_TAG, offset, offset)
                    .firstOrNull()?.let { stringAnnotation ->
                        annotatedLinkString = text.annotationUrl()
                        uriHandler.openUri(stringAnnotation.item)
                        true
                    } ?: run {
                    false
                }
            } ?: run {
                false
            }
        }
    }
    Text(
        annotatedLinkString,
        modifier.then(pressIndicator),
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        emptyMap(),
        {
            layoutResult.value = it
            onTextLayout(it)
        },
        style
    )
}

private val NoPressGesture: suspend PressGestureScope.(Offset) -> Boolean = { false }

suspend fun PointerInputScope.detectPressAndTapOrUnconsumed(
    onPress: suspend PressGestureScope.(Offset) -> Boolean = NoPressGesture,
    onTap: ((Offset) -> Boolean)? = null
) = coroutineScope {
    val pressScope = PressGestureScopeImpl(this@detectPressAndTapOrUnconsumed)
    forEachGesture {
        coroutineScope {
            pressScope.reset()
            awaitPointerEventScope {
                val down = awaitFirstDown()
                if (onPress !== NoPressGesture) {
                    launch {
                        if (pressScope.onPress(down.position)) {
                            down.consumeDownChange()
                        }
                    }
                }
                val up = waitForUpOrCancellation()
                if (up == null) {
                    pressScope.cancel()
                } else {
                    pressScope.release()
                    if (onTap?.invoke(up.position) == true) {
                        up.consumeDownChange()
                    }
                }
            }
        }
    }
}

private class PressGestureScopeImpl(
    density: Density
) : PressGestureScope, Density by density {
    private var isReleased = false
    private var isCanceled = false
    private val mutex = Mutex(locked = false)

    /**
     * Called when a gesture has been canceled.
     */
    fun cancel() {
        isCanceled = true
        mutex.unlock()
    }

    /**
     * Called when all pointers are up.
     */
    fun release() {
        isReleased = true
        mutex.unlock()
    }

    /**
     * Called when a new gesture has started.
     */
    fun reset() {
        mutex.tryLock() // If tryAwaitRelease wasn't called, this will be unlocked.
        isReleased = false
        isCanceled = false
    }

    override suspend fun awaitRelease() {
        if (!tryAwaitRelease()) {
            throw GestureCancellationException("The press gesture was canceled.")
        }
    }

    override suspend fun tryAwaitRelease(): Boolean {
        if (!isReleased && !isCanceled) {
            mutex.lock()
        }
        return isReleased
    }
}
