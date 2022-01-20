package com.lijie.jiancang.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.lijie.jiancang.db.entity.CollectionComplete
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
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(collections, key = {
                it.collection.id
            }) {
                CollectionItem(it, { it1 ->
                    viewModel.deleteCollection(it1)
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

