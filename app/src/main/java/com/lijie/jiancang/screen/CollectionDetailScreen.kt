package com.lijie.jiancang.screen

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.LabelQuote
import com.lijie.jiancang.ui.compose.AutoLinkText
import com.lijie.jiancang.ui.compose.TopAppBar
import com.lijie.jiancang.ui.compose.WebView
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.io.File

@ExperimentalUnitApi
@Composable
fun CollectionDetailScreen(collectionComplete: CollectionComplete) {
    val onBackPressedDispatcher =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Screen(topBar = {
        TopAppBar(title = { Text(text = collectionComplete.collection.title ?: "") },
            navigationIcon = {
                IconButton(onClick = {
                    onBackPressedDispatcher?.onBackPressed()
                }) {
                    Icon(Icons.Default.Close, contentDescription = "关闭")
                }
            })
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (collectionComplete.collection.type) {
                CollectionType.Image -> {
                    ImageContent(collectionComplete = collectionComplete)
                }
                CollectionType.MD -> {
                    MDContent(collectionComplete = collectionComplete)
                }
                CollectionType.Text -> {
                    TextContent(collectionComplete = collectionComplete)
                }
                CollectionType.URL -> {
                    URLContent(collectionComplete = collectionComplete)
                }
            }
        }
    }
}

@Composable
private fun TextContent(collectionComplete: CollectionComplete) {
    AutoLinkText(text = collectionComplete.collection.content)
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

@Composable
private fun MDContent(collectionComplete: CollectionComplete) {
    val mdString = File(collectionComplete.collection.content).readText()
    MarkdownText(markdown = mdString)
}

@ExperimentalUnitApi
@Composable
private fun URLContent(collectionComplete: CollectionComplete) {
    WebView(url = collectionComplete.collection.content)
}

@ExperimentalUnitApi
@Preview
@Composable
private fun Preview() {
    CollectionDetailScreen(
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