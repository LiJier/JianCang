package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.data.Result
import com.lijie.jiancang.data.db.entity.Label
import com.lijie.jiancang.data.source.ICollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelManagerViewModel @Inject constructor(
    private val repository: ICollectionRepository
) : ViewModel() {

    private val _labelsResult = MutableStateFlow<Result<List<Label>>>(Result.Success(listOf()))
    private val _labels = MutableStateFlow<List<Label>>(listOf())
    val labelsResult = _labelsResult.asStateFlow()
    val labels = _labels.asStateFlow()

    private val _addDeleteLabelResult = MutableStateFlow<Result<Boolean>>(Result.Success(false))
    val addDeleteLabelResult = _addDeleteLabelResult.asStateFlow()

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

    fun addLabel(labelName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _addDeleteLabelResult.value = Result.Loading
            val result = repository.addLabel(labelName)
            _addDeleteLabelResult.value = result
            if (result is Result.Success) {
                queryLabel()
            }
        }
    }

    fun deleteLabel(label: Label) {
        viewModelScope.launch(Dispatchers.IO) {
            _addDeleteLabelResult.value = Result.Loading
            val result = repository.deleteLabel(label)
            _addDeleteLabelResult.value = result
            if (result is Result.Success) {
                queryLabel()
            }
        }
    }

}