package com.lijie.jiancang.viewmodel

import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.Label
import com.lijie.jiancang.repository.IRepository
import com.lijie.jiancang.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LabelCollectionViewModel @Inject constructor(repository: IRepository) :
    BaseViewModel(repository) {

    val labelsRes = ResFlow(listOf<Label>())
    val collectionCompletesRes = ResFlow(listOf<CollectionComplete>())
    val deleteCollectionRes = ResFlow(false)
    var currentLabel: Label? = null

    fun queryLabels() {
        launch(labelsRes) {
            getAllLabels()
        }
    }

    fun queryLabelCollection(label: Label) {
        currentLabel = label
        launch(collectionCompletesRes) {
            getLabelCollections(label.id)
        }
    }

    fun deleteCollection(collectionComplete: CollectionComplete) {
        launch(deleteCollectionRes) {
            val deleteResult = deleteCollection(collectionComplete)
            if (deleteResult is Result.Success) {
                currentLabel?.let { queryLabelCollection(it) }
            }
            deleteResult
        }
    }

}