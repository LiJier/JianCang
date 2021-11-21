package com.lijie.jiancang.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lijie.jiancang.ui.compose.TopAppBar

object LabelManagerScreen : Screen("label_manager_screen")

@Composable
fun LabelManagerScreen() {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Screen(topBar = {
        TopAppBar(title = { Text(text = "标签管理") }, navigationIcon = {
            IconButton(onClick = {
                onBackPressedDispatcher?.onBackPressed()
            }) {
                Icon(Icons.Default.Close, contentDescription = "关闭")
            }
        })
    }) {

    }
}

@Preview
@Composable
fun LabelManagerPreview() {
    LabelManagerScreen()
}