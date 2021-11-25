package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.data.Result
import com.lijie.jiancang.data.db.entity.CollectionComplete
import com.lijie.jiancang.data.db.entity.CollectionType
import com.lijie.jiancang.data.source.ICollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(
    private val repository: ICollectionRepository
) : ViewModel() {

    private lateinit var collectionComplete: CollectionComplete
    private var mdContent: String = ""
    private val _saveResult = MutableStateFlow<Result<Boolean>>(Result.Success(false))
    val savedResult = _saveResult.asStateFlow()

    fun setCollectionComplete(collectionComplete: CollectionComplete) {
        this.collectionComplete = collectionComplete
    }

    fun setNewContent(content: String) {
        val type = collectionComplete.collection.type
        if (type == CollectionType.Text) {
            collectionComplete.collection.content = content
        } else if (type == CollectionType.MD) {
            mdContent = content
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            _saveResult.value = Result.Loading
            _saveResult.value = repository.updateCollection(collectionComplete, mdContent)
        }
    }

}