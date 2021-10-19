package com.lijie.jiancang.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lijie.jiancang.R
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.Content
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
            LazyColumn {
                items(collections) {
                    CollectionItem(it)
                }
            }
        }
        viewModel.queryCollections()
    }
}

@Composable
fun CollectionItem(collection: Collection) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = 2.dp
        ) {
            val content: List<Content> =
                Gson().fromJson(collection.content, object : TypeToken<List<Content>>() {}.type)
            Column(modifier = Modifier.padding(8.dp)) {
                content.forEach {
                    Text(text = it.content)
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