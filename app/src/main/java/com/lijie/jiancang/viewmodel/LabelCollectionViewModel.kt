package com.lijie.jiancang.viewmodel

import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.Label
import com.lijie.jiancang.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LabelCollectionViewModel @Inject constructor(repository: IRepository) :
    BaseViewModel(repository) {

    val labelsRes = ResFlow(listOf<Label>())
    val collectionCompletesRes = ResFlow(listOf<CollectionComplete>())

    fun queryLabels() {
        launch(labelsRes) {
            getAllLabels()
        }
    }

    fun queryLabelCollection(labelId: Long) {
        launch(collectionCompletesRes) {
            getLabelCollections(labelId)
        }
    }

}