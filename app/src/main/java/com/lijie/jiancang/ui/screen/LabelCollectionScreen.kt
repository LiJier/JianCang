package com.lijie.jiancang.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.repository.PreviewRepository
import com.lijie.jiancang.ui.compose.CollectionItem
import com.lijie.jiancang.viewmodel.LabelCollectionViewModel

@ExperimentalCoilApi
@Composable
fun LabelCollectionScreen(
    viewModel: LabelCollectionViewModel = hiltViewModel(),
    onCollectionItemClick: (CollectionComplete) -> Unit
) {
    Row(Modifier.fillMaxSize()) {
        val labels by viewModel.labelsRes.dataFlow.collectAsState()
        LaunchedEffect(Unit) {
            viewModel.queryLabels()
        }
        LaunchedEffect(labels) {
            if (viewModel.currentLabel == null && labels.isNotEmpty()) {
                viewModel.currentLabel = labels[0]
                viewModel.currentLabel?.let { viewModel.queryLabelCollection(it) }
            }
        }
        LazyColumn(Modifier.weight(0.2f)) {
            items(labels) {
                Text(text = it.name, modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        viewModel.currentLabel = it
                        viewModel.queryLabelCollection(it)
                    })
            }
        }
        val collectionCompletes by viewModel.collectionCompletesRes.dataFlow.collectAsState()
        LazyColumn(Modifier.weight(0.8f)) {
            items(collectionCompletes, key = {
                it.collection.id
            }) {
                CollectionItem(it, { it1 ->
                    viewModel.deleteCollection(it1)
                }, { it1 ->
                    onCollectionItemClick(it1)
                })
            }
        }
    }
}

@ExperimentalCoilApi
@Preview
@Composable
fun LabelCollectionPreview() {
    LabelCollectionScreen(LabelCollectionViewModel(PreviewRepository)) {}
}