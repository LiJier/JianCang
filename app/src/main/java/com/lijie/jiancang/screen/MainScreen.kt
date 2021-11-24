package com.lijie.jiancang.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.R
import com.lijie.jiancang.data.db.entity.CollectionComplete
import com.lijie.jiancang.data.db.entity.CollectionType
import com.lijie.jiancang.data.source.PreviewCollectionRepository
import com.lijie.jiancang.ext.toTime
import com.lijie.jiancang.ui.compose.AutoLinkText
import com.lijie.jiancang.ui.compose.TopAppBar
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.viewmodel.MainViewModel
import java.io.File

object MainScreen : Screen("main_screen")

val LocalMainViewModel = staticCompositionLocalOf {
    MainViewModel(PreviewCollectionRepository())
}

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onItemClick: (CollectionComplete) -> Unit
) {
    CompositionLocalProvider(LocalMainViewModel provides viewModel) {
        val context = LocalContext.current
        Screen(topBar = {
            TopAppBar(
                title = { Text(text = context.getString(R.string.app_name)) }
            )
        }, drawerContent = {
            MainDrawerContent()
        }) {
            val collections by viewModel.collections.collectAsState()
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(collections) {
                    CollectionItem(it, onItemClick)
                }
            }
            LaunchedEffect(Unit) {
                viewModel.queryCollections()
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun CollectionItem(
    collectionComplete: CollectionComplete,
    onItemClick: (CollectionComplete) -> Unit
) {
    val theme by LocalViewModel.current.theme.collectAsState()
    val viewModel = LocalMainViewModel.current
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
    MainScreen(MainViewModel(PreviewCollectionRepository())) {}
}


