package com.lijie.jiancang.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.data.db.entity.Label
import com.lijie.jiancang.data.source.PreviewCollectionRepository
import com.lijie.jiancang.ext.toast
import com.lijie.jiancang.ui.compose.TopAppBar
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.viewmodel.LabelManagerViewModel

object LabelManagerScreen : Screen("label_manager_screen")

private val LocalLabelManagerViewModel = staticCompositionLocalOf {
    LabelManagerViewModel(PreviewCollectionRepository)
}

@Composable
fun LabelManagerScreen(
    viewModel: LabelManagerViewModel
) {
    CompositionLocalProvider(LocalLabelManagerViewModel provides viewModel) {
        val onBackPressedDispatcher =
            LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        Screen(topBar = {
            TopAppBar(title = { Text(text = "标签管理") }, navigationIcon = {
                IconButton(onClick = {
                    onBackPressedDispatcher?.onBackPressed()
                }) {
                    Icon(Icons.Default.Close, contentDescription = "关闭")
                }
            }, actions = {
                var showAddLabelDialog by remember { mutableStateOf(false) }
                if (showAddLabelDialog) {
                    Dialog(onDismissRequest = { showAddLabelDialog = false }) {
                        AddLabelDialog(onCancel = { showAddLabelDialog = false }, onConfirm = {
                            showAddLabelDialog = false
                            viewModel.addLabel(it)
                        })
                    }
                }
                IconButton(onClick = {
                    showAddLabelDialog = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "添加")
                }
            })
        }) {
            Column(modifier = Modifier.padding(16.dp)) {
                val labels by viewModel.labels.collectAsState()
                LabelManagerFlow(labels)
                LaunchedEffect(Unit) {
                    viewModel.queryLabel()
                }
            }
        }
    }
}

@Composable
fun LabelManagerFlow(labels: List<Label>) {
    val viewModel = LocalLabelManagerViewModel.current
    val theme by LocalViewModel.current.theme.collectAsState()
    FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
        labels.forEach { label ->
            Text(
                text = label.name,
                color = Color.White,
                modifier = Modifier
                    .background(
                        theme.primary,
                        shape = Shapes.small
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                viewModel.deleteLabel(label)
                            }
                        )
                    }
            )
        }
    }
}

@Composable
fun AddLabelDialog(onCancel: () -> Unit, onConfirm: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(color = Color.White, shape = Shapes.medium)
    ) {
        val theme by LocalViewModel.current.theme.collectAsState()
        var labelName by remember { mutableStateOf("") }
        val textStyle = LocalTextStyle.current.merge(
            TextStyle(
                color = theme.primary
            )
        )
        OutlinedTextField(value = labelName, onValueChange = {
            labelName = it
        }, textStyle = textStyle, modifier = Modifier.padding(16.dp))
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Gray)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "取消",
                textAlign = TextAlign.Center,
                style = textStyle,
                modifier = Modifier
                    .weight(1F)
                    .clickable {
                        onCancel()
                    })
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color.Gray)
            )
            Text(
                text = "确定",
                textAlign = TextAlign.Center,
                style = textStyle,
                modifier = Modifier
                    .weight(1F)
                    .clickable {
                        if (labelName.isNotEmpty()) {
                            onConfirm(labelName)
                        } else {
                            "标签名不能为空".toast()
                        }
                    })
        }
    }
}

@Preview
@Composable
fun LabelManagerPreview() {
    LabelManagerScreen(LabelManagerViewModel(PreviewCollectionRepository))
}

@Preview
@Composable
fun AddLabelDialogPreview() {
    AddLabelDialog({}, {})
}