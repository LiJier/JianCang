package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.data.Result
import com.lijie.jiancang.data.db.entity.CollectionComplete
import com.lijie.jiancang.data.source.ICollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ICollectionRepository
) : ViewModel() {

    private val _collectionsLoading =
        MutableStateFlow<Result<List<CollectionComplete>>>(Result.None)
    private val _collections = MutableStateFlow<List<CollectionComplete>>(listOf())
    val collectionsLoading = _collectionsLoading.asStateFlow()
    val collections = _collections.asStateFlow()

    fun setCollections(collectionCompletes: List<CollectionComplete>) {
        _collections.value = collectionCompletes
    }

    fun queryCollections() {
        viewModelScope.launch(Dispatchers.IO) {
            _collectionsLoading.value = Result.Loading
            val result = repository.getAllCollection()
            if (result is Result.Success) {
                _collections.value = result.data
            }
            _collectionsLoading.value = result
        }
    }

    fun deleteCollection(collectionComplete: CollectionComplete) {
        viewModelScope.launch(Dispatchers.IO) {
            _collectionsLoading.value = Result.Loading
            val deleteResult = repository.deleteCollection(collectionComplete)
            if (deleteResult is Result.Success) {
                queryCollections()
            }
        }
    }

}