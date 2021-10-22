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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.ContentType
import com.lijie.jiancang.ext.toast
import com.lijie.jiancang.ui.compose.TopAppBar
import com.lijie.jiancang.ui.compose.WebView
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.viewmodel.AddCollectionViewModel

@SuppressLint("SetJavaScriptEnabled")
@ExperimentalCoilApi
@Composable
fun AddCollectionScreen(
    viewModel: AddCollectionViewModel = viewModel(),
    content: String = "内容",
    type: CollectionType = CollectionType.Text
) {
    val theme = LocalViewModel.current.themeFlow.value
    viewModel.original = content
    var editContent by remember { mutableStateOf(content) }
    var contentType: ContentType = ContentType.Text
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
                    if (type != CollectionType.URL) {
                        viewModel.addContent(contentType, editContent)
                    }
                    viewModel.saveCollection()
                }) {
                    Icon(Icons.Default.Done, contentDescription = "保存")
                }
                val save by viewModel.save.collectAsState()
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
                    contentType = ContentType.Text
                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                CollectionType.Image -> {
                    contentType = ContentType.Image
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
                CollectionType.URL -> {
                    WebView(url = content)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "标签")
            Spacer(modifier = Modifier.height(8.dp))
            val labelState by viewModel.labels.collectAsState()
            FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
                labelState.forEach { label ->
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
            LaunchedEffect(Unit) { viewModel.queryLabel() }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "想法")
            Spacer(modifier = Modifier.height(8.dp))
            var idea by remember { mutableStateOf(viewModel.idea.value) }
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

@ExperimentalCoilApi
@Preview
@Composable
private fun Preview() {
    AddCollectionScreen(AddCollectionViewModel(true))
}