package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import com.lijie.jiancang.db.entity.Collection
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.db.entity.Label
import com.lijie.jiancang.ext.launch
import com.lijie.jiancang.repository.IRepository
import com.lijie.jiancang.repository.ResFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddCollectionViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val collection = Collection(
        type = CollectionType.Text,
        original = "",
        title = "",
        content = "",
        idea = "",
    )

    val labelsRes = ResFlow<List<Label>>(listOf())
    val savedRes = ResFlow(false)

    fun setType(type: CollectionType) {
        collection.type = type
    }

    fun setOriginal(original: String) {
        collection.original = original
    }

    fun setTitle(title: String) {
        collection.title = title
    }

    fun setContent(content: String) {
        collection.content = content
    }

    fun setIdea(idea: String) {
        collection.idea = idea
    }

    fun queryLabel() {
        launch(labelsRes) {
            repository.getAllLabels()
        }
    }

    fun saveCollection() {
        launch(savedRes) {
            repository.saveCollection(collection, labelsRes.dataFlow.value)
        }
    }

}