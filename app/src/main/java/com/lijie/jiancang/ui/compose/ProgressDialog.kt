package com.lijie.jiancang.ui.compose

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ProgressDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
private fun Preview() {
    ProgressDialog({})
}