package com.lijie.jiancang.screen

import android.annotation.SuppressLint
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.data.Result
import com.lijie.jiancang.data.db.entity.CollectionType
import com.lijie.jiancang.data.db.entity.Label
import com.lijie.jiancang.data.source.PreviewCollectionRepository
import com.lijie.jiancang.ext.findUrl
import com.lijie.jiancang.ext.toast
import com.lijie.jiancang.ui.compose.ProgressDialog
import com.lijie.jiancang.ui.compose.TopAppBar
import com.lijie.jiancang.ui.compose.WebView
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.viewmodel.AddCollectionViewModel
import com.overzealous.remark.Remark
import dev.jeziellago.compose.markdowntext.MarkdownText

object AddCollectionScreen : Screen("add_collection_screen")

private val LocalAddCollectionViewModel = staticCompositionLocalOf {
    AddCollectionViewModel(PreviewCollectionRepository())
}

@ExperimentalUnitApi
@SuppressLint("SetJavaScriptEnabled")
@ExperimentalCoilApi
@Composable
fun AddCollectionScreen(
    viewModel: AddCollectionViewModel,
    content: String = "内容",
    type: CollectionType = CollectionType.Text
) {
    CompositionLocalProvider(LocalAddCollectionViewModel provides viewModel) {
        viewModel.setOriginal(content)
        val onBackPressedDispatcher =
            LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        Screen(topBar = {
            TopAppBar(
                title = { Text(text = "添加") },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressedDispatcher?.onBackPressed()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveCollection()
                    }) {
                        Icon(Icons.Default.Done, contentDescription = "保存")
                    }
                }
            )
            val savedLoading by viewModel.savedResult.collectAsState()
            when (savedLoading) {
                is Result.Success -> {
                    LaunchedEffect(savedLoading) {
                        "保存成功".toast()
                        onBackPressedDispatcher?.onBackPressed()
                    }
                }
                is Result.Error -> {
                    LaunchedEffect(savedLoading) {
                        "保存失败".toast()
                    }
                }
                is Result.Loading -> {
                    ProgressDialog(onDismissRequest = { })
                }
                else -> {}
            }
        }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                var editTitle by remember {
                    mutableStateOf(
                        content.substring(0, if (content.length > 6) 6 else content.length)
                    )
                }
                viewModel.setTitle(editTitle)
                Text(text = "标题", modifier = Modifier.padding(vertical = 8.dp))
                OutlinedTextField(
                    value = editTitle,
                    onValueChange = {
                        editTitle = it
                        viewModel.setTitle(editTitle)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(text = "内容", modifier = Modifier.padding(vertical = 8.dp))
                if (type == CollectionType.Text) {
                    TextType(content) { title, _ ->
                        editTitle = title
                        viewModel.setTitle(editTitle)
                    }
                } else if (type == CollectionType.Image) {
                    viewModel.setType(type)
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = rememberImagePainter(data = content) {
                                size(OriginalSize)
                            },
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .constrainAs(createRef()) {
                                    start.linkTo(parent.start)
                                    width = Dimension.percent(0.5F)
                                }
                                .aspectRatio(1F)
                        )
                    }
                }
                Text(text = "标签", modifier = Modifier.padding(vertical = 8.dp))
                val labels by viewModel.labels.collectAsState()
                LabelsFlow(labels)
                LaunchedEffect(Unit) { viewModel.queryLabel() }
                Text(text = "想法", modifier = Modifier.padding(vertical = 8.dp))
                var idea by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = idea,
                    onValueChange = { s ->
                        idea = s
                        viewModel.setIdea(idea)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@ExperimentalUnitApi
@Composable
fun TextType(content: String, onLoadComplete: (String, String) -> Unit) {
    val viewModel = LocalAddCollectionViewModel.current
    val url by remember { mutableStateOf(content.findUrl().orEmpty()) }
    var type by remember {
        mutableStateOf(
            if (url.isEmpty()) {
                CollectionType.Text
            } else {
                CollectionType.URL
            }
        )
    }
    var editContent by remember { mutableStateOf(if (type == CollectionType.Text) content else url) }
    var htmlContent by remember { mutableStateOf("") }
    viewModel.setType(type)
    viewModel.setContent(editContent)
    Column(modifier = Modifier.fillMaxWidth()) {
        Box {
            if (type != CollectionType.Text) {
                var showProgress by remember { mutableStateOf(true) }
                if (showProgress) {
                    ProgressDialog(onDismissRequest = { showProgress = false })
                }
                WebView(url = url, onLoadComplete = { title, html ->
                    onLoadComplete(title, html)
                    htmlContent = html
                    showProgress = false
                }, modifier = Modifier.size(1.dp))
            }
            if (type == CollectionType.MD) {
                MarkdownText(markdown = editContent)
            } else {
                OutlinedTextField(
                    value = editContent,
                    onValueChange = {
                        editContent = it
                        viewModel.setContent(editContent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = type == CollectionType.Text
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = type == CollectionType.Text, onCheckedChange = {
                if (it) {
                    type = CollectionType.Text
                    editContent = content
                    viewModel.setType(type)
                    viewModel.setContent(editContent)
                }
            })
            Text(text = "文本")
            Spacer(modifier = Modifier.width(16.dp))
            Checkbox(checked = type == CollectionType.URL, onCheckedChange = {
                if (it) {
                    type = CollectionType.URL
                    editContent = url
                    viewModel.setType(type)
                    viewModel.setContent(editContent)
                }
            })
            Text(text = "链接")
            Spacer(modifier = Modifier.width(16.dp))
            Checkbox(checked = type == CollectionType.MD, onCheckedChange = {
                if (it) {
                    type = CollectionType.MD
                    editContent = Remark().convert(htmlContent)
                    viewModel.setType(type)
                    viewModel.setContent(editContent)
                }
            })
            Text(text = "离线MD")
        }
    }
}

@Composable
fun LabelsFlow(labels: List<Label>) {
    val theme by LocalViewModel.current.theme.collectAsState()
    FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
        labels.forEach { label ->
            var check by remember { mutableStateOf(label.check) }
            Text(
                text = label.name,
                color = Color.White,
                modifier = Modifier
                    .background(
                        if (check) theme.primaryVariant else theme.primary,
                        shape = Shapes.small
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable {
                        check = check.not()
                        label.check = check
                    }
            )
        }
    }
}

@ExperimentalUnitApi
@ExperimentalCoilApi
@Preview
@Composable
fun AddCollectionPreview() {
    AddCollectionScreen(AddCollectionViewModel(PreviewCollectionRepository()).apply {
        setLabels(
            arrayListOf(
                Label(name = "电影"),
                Label(name = "图书"),
                Label(name = "歌曲")
            )
        )
    })
}

