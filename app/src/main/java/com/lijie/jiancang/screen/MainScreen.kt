package com.lijie.jiancang.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowRow
import com.lijie.jiancang.R
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.ext.annotated
import com.lijie.jiancang.ext.toTime
import com.lijie.jiancang.ui.theme.Shapes
import com.lijie.jiancang.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    Screen(topBar = {
        TopAppBar(
            title = { Text(text = context.getString(R.string.app_name)) }
        )
    }) {
        val collections by viewModel.collections.collectAsState()
        if (collections.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(collections) {
                    CollectionItem(it)
                }
            }
        }
        LaunchedEffect(key1 = Unit, block = {
            viewModel.queryCollections()
        })
    }
}

@Composable
fun CollectionItem(collectionComplete: CollectionComplete) {
    val theme = LocalViewModel.current.themeFlow.value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                val uriHandler = LocalUriHandler.current
                Text(text = collectionComplete.collection.createTime.toTime(), fontSize = 10.sp)
                Spacer(modifier = Modifier.height(8.dp))
                var annotatedLinkString by remember {
                    mutableStateOf(collectionComplete.content.first().content.annotated())
                }
                ClickableText(
                    text = annotatedLinkString,
                    onPress = {
                        annotatedLinkString
                            .getStringAnnotations("URL", it, it)
                            .firstOrNull()?.let {
                                annotatedLinkString =
                                    collectionComplete.content.first().content.annotated(true)
                            }
                    },
                    onClick = {
                        annotatedLinkString
                            .getStringAnnotations("URL", it, it)
                            .firstOrNull()?.let { stringAnnotation ->
                                annotatedLinkString =
                                    collectionComplete.content.first().content.annotated(false)
                                uriHandler.openUri(stringAnnotation.item)
                            }
                    })
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

@Preview
@Composable
private fun Preview() {
    MainScreen(MainViewModel(true))
}