package com.lijie.jiancang.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.repository.Nullable
import com.lijie.jiancang.ui.compose.CollectionItem
import com.lijie.jiancang.viewmodel.MainViewModel

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onItemClick: (CollectionComplete) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val collections by viewModel.collectionsRes.dataFlow.collectAsState()
        var showDialog by remember { mutableStateOf(false) }
        var deleteItem: Nullable<CollectionComplete> by remember { mutableStateOf(Nullable(null)) }
        if (showDialog) {
            AlertDialog(onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        deleteItem.data?.let {
                            viewModel.deleteCollection(it)
                        }
                    }) {
                        Text(text = "删除")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = false
                    }) {
                        Text(text = "取消")
                    }
                },
                title = { Text(text = "确认删除？") }
            )
        }
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(collections, key = {
                it.collection.id
            }) {
                CollectionItem(it, { it1 ->
                    deleteItem = Nullable(it1)
                    showDialog = true
                }, { it1 ->
                    onItemClick(it1)
                })
            }
        }
        LaunchedEffect(Unit) {
            viewModel.queryCollections()
        }
    }
}

