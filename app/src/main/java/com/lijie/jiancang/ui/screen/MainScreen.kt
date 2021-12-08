package com.lijie.jiancang.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.ext.toTime
import com.lijie.jiancang.repository.PreviewRepository
import com.lijie.jiancang.ui.compose.AutoLinkText
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.ui.theme.theme
import com.lijie.jiancang.viewmodel.MainViewModel
import java.io.File

object MainScreen : Screen("main_screen")

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onItemClick: (CollectionComplete) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val collections by viewModel.collectionsRes.dataFlow.collectAsState()
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(collections) {
                CollectionItem(viewModel, it, onItemClick)
            }
        }
        LaunchedEffect(Unit) {
            viewModel.queryCollections()
        }
    }
}

@ExperimentalCoilApi
@Composable
fun CollectionItem(
    viewModel: MainViewModel,
    collectionComplete: CollectionComplete,
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
                            viewModel.deleteCollection(collectionComplete)
                        },
                        onTap = {
                            onItemClick.invoke(collectionComplete)
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
                        AutoLinkText(
                            text = collection.content
                        )
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

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Preview
@Composable
fun MainPreview() {
    MainScreen(MainViewModel(PreviewRepository)) {}
}


