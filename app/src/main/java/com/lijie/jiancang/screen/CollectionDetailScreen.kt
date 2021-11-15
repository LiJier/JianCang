package com.lijie.jiancang.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.LabelQuote
import com.lijie.jiancang.ext.toast
import com.lijie.jiancang.ui.compose.AutoLinkText
import com.lijie.jiancang.ui.compose.TopAppBar
import com.lijie.jiancang.ui.compose.WebView
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
    CompositionLocalProvider(LocalCollectionDetailsViewModel provides viewModel) {
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsWithImePadding()
            ) {
                item {
                    when (collectionComplete.collection.type) {
                        CollectionType.Image -> {
                            ImageContent(collectionComplete = collectionComplete)
                        }
                        CollectionType.MD -> {
                            val mdString = File(collectionComplete.collection.content).readText()
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
            }
        }
    }
}

@Composable
private fun TextContent(collectionComplete: CollectionComplete, isEdit: Boolean) {
    val viewModel = LocalCollectionDetailsViewModel.current
    if (isEdit) {
        var text by remember { mutableStateOf(collectionComplete.collection.content) }
        TextField(value = text, onValueChange = {
            text = it
            viewModel.setNewContent(text)
        })
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
        var text by remember { mutableStateOf(mdString) }
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.setNewContent(text)
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