package com.lijie.jiancang.ui.compose

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.ext.findIntent
import com.lijie.jiancang.ext.toTime
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.ui.theme.theme
import java.io.File

@ExperimentalCoilApi
@Composable
fun CollectionItem(
    collectionComplete: CollectionComplete,
    onItemLongClick: (CollectionComplete) -> Unit,
    onItemClick: (CollectionComplete) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            onItemLongClick(collectionComplete)
                        },
                        onTap = {
                            onItemClick(collectionComplete)
                        }
                    )
                },
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = collectionComplete.collection.createTime.toTime(), fontSize = 10.sp)
                Spacer(modifier = Modifier.height(8.dp))
                val title = collectionComplete.collection.title ?: ""
                if (title.isNotEmpty()) {
                    Text(text = title, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                val collection = collectionComplete.collection
                when (collection.type) {
                    CollectionType.Text -> {
                        AutoLinkText(
                            text = collection.content
                        )
                    }
                    CollectionType.URL -> {
                        val findIntent = collection.content.findIntent()
                        Column {
                            AutoLinkText(
                                text = collection.content
                            )
                            FlowRow {
                                findIntent.forEach {
                                    Image(
                                        painter = BitmapPainter((it as BitmapDrawable).bitmap.asImageBitmap()),
                                        contentDescription = "",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                    CollectionType.Image -> {
                        Image(
                            painter = rememberImagePainter(
                                data = File(collection.content),
                                builder = {
                                    size(OriginalSize)
                                },
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(80.dp, 80.dp)
                        )
                    }
                    CollectionType.MD -> {
                        Text(text = File(collection.content).name)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(mainAxisSpacing = 2.dp, crossAxisSpacing = 2.dp) {
                    collectionComplete.labelQuote.forEach {
                        Text(
                            text = it.labelName,
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    theme.primary,
                                    shape = Shapes.small
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}