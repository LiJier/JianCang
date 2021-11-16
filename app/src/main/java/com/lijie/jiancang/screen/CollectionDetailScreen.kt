package com.lijie.jiancang.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.LabelQuote
import com.lijie.jiancang.ext.toast
import com.lijie.jiancang.ui.compose.*
import com.lijie.jiancang.viewmodel.CollectionDetailsViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.io.File

private val LocalCollectionDetailsViewModel = staticCompositionLocalOf {
    CollectionDetailsViewModel()
}

@ExperimentalAnimatedInsets
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun CollectionDetailScreen(
    viewModel: CollectionDetailsViewModel = viewModel(),
    collectionComplete: CollectionComplete
) {
    CompositionLocalProvider(
        LocalCollectionDetailsViewModel provides viewModel,
        LocalSelectionTop provides SelectionTop(0)
    ) {
        val onBackPressedDispatcher =
            LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        viewModel.setCollectionComplete(collectionComplete)
        var isEdit by remember { mutableStateOf(false) }
        Screen(topBar = {
            TopAppBar(title = { Text(text = collectionComplete.collection.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                },
                actions = {
                    val type = collectionComplete.collection.type
                    if (type == CollectionType.Text || type == CollectionType.MD) {
                        IconButton(onClick = {
                            isEdit = isEdit.not()
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "编辑")
                        }
                        if (isEdit) {
                            IconButton(onClick = {
                                viewModel.save()
                            }) {
                                Icon(Icons.Default.Done, contentDescription = "保存")
                                val save by viewModel.saved.collectAsState()
                                when (save) {
                                    true -> {
                                        LaunchedEffect(Unit) {
                                            "保存成功".toast()
                                            onBackPressedDispatcher?.onBackPressed()
                                        }
                                    }
                                    false -> {
                                        "保存失败".toast()
                                    }
                                }
                            }
                        }
                    }
                })
        }, modifier = Modifier.fillMaxSize()) {
            var height by remember { mutableStateOf(0) }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { height = it.height }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
//                    val view = LocalView.current
//                    var isVisible by remember { mutableStateOf(false) }
//                    var imeBottom by remember { mutableStateOf(0) }
//                    var maxImeBottom by remember { mutableStateOf(0) }
                    val scrollState = rememberScrollState()
//                    ViewCompat.setOnApplyWindowInsetsListener(
//                        view,
//                        OnApplyWindowInsetsListener { _, wic ->
//                            isVisible = wic.isVisible(WindowInsetsCompat.Type.ime())
//                            return@OnApplyWindowInsetsListener wic
//                        })
//                    ViewCompat.setWindowInsetsAnimationCallback(view, object :
//                        WindowInsetsAnimationCompat.Callback(DISPATCH_MODE_STOP) {
//
//                        override fun onProgress(
//                            wic: WindowInsetsCompat,
//                            runningAnimations: MutableList<WindowInsetsAnimationCompat>
//                        ): WindowInsetsCompat {
//                            imeBottom = wic.getInsets(WindowInsetsCompat.Type.ime()).bottom
//                            return wic
//                        }
//
//                    })
//                    LaunchedEffect(imeBottom) {
//                        if (maxImeBottom < imeBottom) {
//                            maxImeBottom = imeBottom
//                        }
//                        val top = viewModel.selectionLineTop
//                        val value = scrollState.value
//                        if (isVisible) {
//                            if ((top - value) > maxImeBottom) {
//                                val to = (maxImeBottom - (height - (top - value))) + 200
//                                scrollState.animateScrollTo(
//                                    (scrollState.value + to).toInt(),
//                                    SpringSpec(stiffness = StiffnessHigh)
//                                )
//                            }
//                        }
//                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                            .imeScroll(height, scrollState)
                    ) {
                        when (collectionComplete.collection.type) {
                            CollectionType.Image -> {
                                ImageContent(collectionComplete = collectionComplete)
                            }
                            CollectionType.MD -> {
                                val mdString =
                                    File(collectionComplete.collection.content).readText()
                                viewModel.setNewContent(mdString)
                                MDContent(mdString, isEdit)
                            }
                            CollectionType.Text -> {
                                TextContent(collectionComplete = collectionComplete, isEdit)
                            }
                            CollectionType.URL -> {
                                URLContent(collectionComplete = collectionComplete)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.imeHeight())
                }
            }
        }
    }
}

@Composable
private fun TextContent(collectionComplete: CollectionComplete, isEdit: Boolean) {
    val viewModel = LocalCollectionDetailsViewModel.current
    if (isEdit) {
        var text by remember { mutableStateOf(collectionComplete.collection.content) }
        OutlinedTextField(
            value = text, onValueChange = {
                text = it
                viewModel.setNewContent(text)
            }
        )
    } else {
        AutoLinkText(text = collectionComplete.collection.content)
    }
}

@Composable
private fun ImageContent(collectionComplete: CollectionComplete) {
    Image(
        painter = rememberImagePainter(data = collectionComplete.collection.content) {
            size(OriginalSize)
        },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth()
    )
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun MDContent(mdString: String, isEdit: Boolean) {
    val viewModel = LocalCollectionDetailsViewModel.current
    if (isEdit) {
        var text by remember { mutableStateOf(TextFieldValue(mdString)) }
        val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
        val selectionTop = LocalSelectionTop.current
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                it.selection
                viewModel.setNewContent(it.text)
                val line = layoutResult.value?.getLineForOffset(it.selection.start) ?: 0
                val top = layoutResult.value?.getLineTop(line) ?: 0F
                selectionTop.value = top.toInt()
            }, onTextLayout = {
                layoutResult.value = it
            })
    } else {
        MarkdownText(markdown = mdString)
    }
}

@ExperimentalUnitApi
@Composable
private fun URLContent(collectionComplete: CollectionComplete) {
    WebView(url = collectionComplete.collection.content)
}

@ExperimentalAnimatedInsets
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalUnitApi
@Preview
@Composable
private fun Preview() {
    CollectionDetailScreen(
        CollectionDetailsViewModel(),
        CollectionComplete(
            Collection(
                type = CollectionType.Text,
                original = "",
                title = "标题",
                content = "预览",
                createTime = System.currentTimeMillis()
            ),
            arrayListOf(LabelQuote(labelName = "歌曲"))
        )
    )
}