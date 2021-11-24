package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.data.Result
import com.lijie.jiancang.data.db.entity.Collection
import com.lijie.jiancang.data.db.entity.CollectionType
import com.lijie.jiancang.data.db.entity.Label
import com.lijie.jiancang.data.source.ICollectionRepository
import com.lijie.jiancang.data.source.PreviewCollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCollectionViewModel @Inject constructor(
    private val repository: ICollectionRepository
) : ViewModel() {

    private val collection = Collection(
        type = CollectionType.Text,
        original = "",
        title = "",
        content = "",
        idea = "",
    )

    private val _labelsResult = MutableStateFlow<Result<List<Label>>>(Result.None)
    private val _labels = MutableStateFlow<List<Label>>(
        if (repository is PreviewCollectionRepository) {
            listOf(
                Label(name = "电影"),
                Label(name = "图书"),
                Label(name = "歌曲")
            )
        } else {
            listOf()
        }
    )
    val labelsLoading = _labelsResult.asStateFlow()
    val labels = _labels.asStateFlow()

    private val _savedResult = MutableStateFlow<Result<Boolean>>(Result.None)
    val savedResult = _savedResult.asStateFlow()

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
        viewModelScope.launch(Dispatchers.IO) {
            _labelsResult.value = Result.Loading
            val result = repository.getAllLabels()
            if (result is Result.Success) {
                _labels.value = result.data
            }
            _labelsResult.value = result
        }
    }

    fun saveCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            _savedResult.value = Result.Loading
            _savedResult.value = repository.saveCollection(collection, labels.value)
        }
    }

}