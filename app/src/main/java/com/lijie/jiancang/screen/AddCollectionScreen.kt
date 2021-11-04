package com.lijie.jiancang.screen

import android.annotation.SuppressLint
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.Label
import com.lijie.jiancang.ext.toast
import com.lijie.jiancang.ui.compose.TopAppBar
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.viewmodel.AddCollectionViewModel

@ExperimentalUnitApi
@SuppressLint("SetJavaScriptEnabled")
@ExperimentalCoilApi
@Composable
fun AddCollectionScreen(
    viewModel: AddCollectionViewModel = viewModel(),
    content: String = "内容",
    type: CollectionType = CollectionType.Text
) {
    viewModel.setType(type)
    viewModel.setTitle(content)
    viewModel.setContent(content)
    var editContent by remember { mutableStateOf(content) }
    Screen(topBar = {
        val onBackPressedDispatcher =
            LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
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
                val save by viewModel.saved.collectAsState()
                when (save) {
                    true -> {
                        "保存成功".toast()
                        onBackPressedDispatcher?.onBackPressed()
                    }
                    false -> {
                        "保存失败".toast()
                    }
                }
            }
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (type) {
                CollectionType.Text -> {
                    OutlinedTextField(
                        value = editContent,
                        onValueChange = {
                            viewModel.setContent(editContent)
                            editContent = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                CollectionType.URL -> {
                    OutlinedTextField(
                        value = editContent,
                        onValueChange = {
                            viewModel.setContent(editContent)
                            editContent = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )
                }
                CollectionType.Image -> {
                    Image(
                        painter = rememberImagePainter(
                            data = content,
                            builder = {
                                size(OriginalSize)
                            },
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "标签")
            Spacer(modifier = Modifier.height(8.dp))
            val labels by viewModel.labels.collectAsState()
            LabelsFlow(labels)
            LaunchedEffect(Unit) { viewModel.queryLabel() }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "想法")
            Spacer(modifier = Modifier.height(8.dp))
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

@Composable
fun LabelsFlow(labels: List<Label>) {
    val theme by LocalViewModel.current.themeFlow.collectAsState()
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
private fun Preview() {
    AddCollectionScreen(AddCollectionViewModel().apply {
        setLabels(
            arrayListOf(
                Label(name = "电影"),
                Label(name = "图书"),
                Label(name = "歌曲")
            )
        )
    })
}

