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
import com.lijie.jiancang.repository.PreviewRepository
import com.lijie.jiancang.viewmodel.LabelCollectionViewModel

object LabelCollectionScreen : Screen("label_collection_screen")

@Composable
fun LabelCollectionScreen(viewModel: LabelCollectionViewModel = hiltViewModel()) {
    Row(Modifier.fillMaxSize()) {
        val labels by viewModel.labelsRes.dataFlow.collectAsState()
        if (labels.isNotEmpty()) {
            LaunchedEffect(labels) {
                viewModel.queryLabelCollection(labels[0].id)
            }
        }
        LazyColumn(Modifier.weight(0.2f)) {
            items(labels) {
                Text(text = it.name, modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        viewModel.queryLabelCollection(it.id)
                    })
            }
        }
        val collectionCompletes by viewModel.collectionCompletesRes.dataFlow.collectAsState()
        LazyColumn(Modifier.weight(0.8f)) {
            items(collectionCompletes) {
                Text(text = it.collection.content, modifier = Modifier.padding(16.dp))
            }
        }
        LaunchedEffect(Unit) {
            viewModel.queryLabels()
        }
    }
}

@Preview
@Composable
fun LabelCollectionPreview() {
    LabelCollectionScreen(LabelCollectionViewModel(PreviewRepository))
}