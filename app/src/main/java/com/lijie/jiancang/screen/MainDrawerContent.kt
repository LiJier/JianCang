package com.lijie.jiancang.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import com.lijie.jiancang.ui.theme.Purple200

@ExperimentalMaterialApi
@Composable
fun MainDrawerContent() {
    Column {
        Image(
            painter = ColorPainter(Purple200),
            contentDescription = "",
            modifier = Modifier
                .fillMaxHeight(0.3F)
                .background(color = Color.Blue)
        )
        ListItem(text = { Text(text = "标签管理") },
            icon = { Icon(Icons.Default.Info, "", tint = Purple200) },
            modifier = Modifier
                .clickable {

                })
        ListItem(text = { Text(text = "偏好设置") },
            icon = { Icon(Icons.Default.Favorite, "", tint = Purple200) },
            modifier = Modifier
                .clickable {

                })
        ListItem(text = { Text(text = "关于简藏") },
            icon = { Icon(Icons.Default.Person, "", tint = Purple200) },
            modifier = Modifier
                .clickable {

                })
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun MainDrawerPreview() {
    MainDrawerContent()
}