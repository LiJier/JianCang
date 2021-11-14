package com.lijie.jiancang.ui.compose

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.lijie.jiancang.ext.URL_TAG
import com.lijie.jiancang.ext.annotationUrl


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
        detectTapGestures(onPress = { pos ->
            layoutResult.value?.let { layoutResult ->
                val offset = layoutResult.getOffsetForPosition(pos)
                annotatedLinkString
                    .getStringAnnotations(URL_TAG, offset, offset)
                    .firstOrNull()?.let {
                        annotatedLinkString = text.annotationUrl(offset)
                    }
            }
            tryAwaitRelease()
            annotatedLinkString = text.annotationUrl()
        }) { pos ->
            layoutResult.value?.let { layoutResult ->
                val offset = layoutResult.getOffsetForPosition(pos)
                annotatedLinkString
                    .getStringAnnotations(URL_TAG, offset, offset)
                    .firstOrNull()?.let { stringAnnotation ->
                        annotatedLinkString = text.annotationUrl()
                        uriHandler.openUri(stringAnnotation.item)
                    } ?: run {

                }
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
