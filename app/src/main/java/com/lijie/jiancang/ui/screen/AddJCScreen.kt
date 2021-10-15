package com.lijie.jiancang.ui.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.ext.toast
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.viewmodel.AddJCViewModel

@Composable
fun AddJCScreen(addJCViewModel: AddJCViewModel = viewModel(), text: String = "内容") {
    Screen(topBar = {
        TopAppBar(
            title = { Text(text = "添加") },
            navigationIcon = {
                val onBackPressedDispatcher =
                    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
                IconButton(onClick = {
                    onBackPressedDispatcher?.onBackPressed()
                }) {
                    Icon(Icons.Default.Close, contentDescription = "关闭")
                }
            },
            actions = {
                IconButton(onClick = {
                    "添加".toast()
                }) {
                    Icon(Icons.Default.Done, contentDescription = "保存")
                }
            }
        )
    }) {
        AddStringContent(addJCViewModel, text)
    }
}

@Composable
fun AddStringContent(addJCViewModel: AddJCViewModel = viewModel(), text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var content by remember { mutableStateOf(text) }
        OutlinedTextField(
            value = content,
            onValueChange = { s ->
                content = s
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "标签")

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
            val label = addJCViewModel.labelFlow.collectAsState()
            label.value.forEach {
                Text(
                    text = it,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.Blue, shape = Shapes.small)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "我的话")

        Spacer(modifier = Modifier.height(8.dp))

        var my by remember { mutableStateOf("") }
        OutlinedTextField(
            value = my,
            onValueChange = { s ->
                my = s
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

}

@Preview
@Composable
fun Preview() {
    AddJCScreen(AddJCViewModel())
}