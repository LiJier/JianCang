package com.lijie.jiancang.ui.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.LabelQuote
import com.lijie.jiancang.ext.toast
import com.lijie.jiancang.repository.PreviewRepository
import com.lijie.jiancang.repository.Result
import com.lijie.jiancang.ui.compose.*
import com.lijie.jiancang.viewmodel.CollectionDetailsViewModel
import java.io.File

object CollectionDetailScreen : Screen("collection_detail_screen")

@ExperimentalAnimatedInsets
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun CollectionDetailScreen(
    viewModel: CollectionDetailsViewModel = hiltViewModel(),
    collectionComplete: CollectionComplete
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
                        }
                    }
                }
            })
        val savedResult by viewModel.savedRes.resultFlow.collectAsState()
        when (savedResult) {
            is Result.Success -> {
                LaunchedEffect(savedResult) {
                    if ((savedResult as Result.Success<Boolean>).data) {
                        "保存成功".toast()
                        onBackPressedDispatcher?.onBackPressed()
                    }
                }
            }
            is Result.Error -> {
                LaunchedEffect(savedResult) {
                    "保存失败".toast()
                }
            }
            is Result.Loading -> {
                ProgressDialog(onDismissRequest = { })
            }
        }
    }, modifier = Modifier.fillMaxSize()) {
        var height by remember { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { height = it.height }
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .padding(8.dp)
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
                        MDContent(viewModel, mdString, isEdit)
                    }
                    CollectionType.Text -> {
                        TextContent(viewModel, collectionComplete, isEdit)
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

@Composable
private fun TextContent(
    viewModel: CollectionDetailsViewModel,
    collectionComplete: CollectionComplete,
    isEdit: Boolean
) {
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
private fun MDContent(viewModel: CollectionDetailsViewModel, mdString: String, isEdit: Boolean) {
    if (isEdit) {
        var text by remember { mutableStateOf(TextFieldValue(mdString)) }
        val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
        StyledTextField(
            value = text,
            onValueChange = {
                text = it
                it.selection
                viewModel.setNewContent(it.text)
                val line = layoutResult.value?.getLineForOffset(it.selection.start) ?: 0
                val top = layoutResult.value?.getLineTop(line) ?: 0F
                selectionTop = top.toInt()
            }, onTextLayout = {
                layoutResult.value = it
            })
    } else {
        Markdown(markdown = mdString)
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
fun CollectionDetailPreview() {
    CollectionDetailScreen(
        CollectionDetailsViewModel(PreviewRepository),
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