package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.data.Result
import com.lijie.jiancang.data.db.entity.Collection
import com.lijie.jiancang.data.db.entity.CollectionComplete
import com.lijie.jiancang.data.db.entity.CollectionType
import com.lijie.jiancang.data.db.entity.LabelQuote
import com.lijie.jiancang.data.source.ICollectionRepository
import com.lijie.jiancang.data.source.PreviewCollectionRepository
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
    private val _collections = MutableStateFlow(
        if (repository is PreviewCollectionRepository) {
            listOf(
                CollectionComplete(
                    Collection(
                        type = CollectionType.URL,
                        original = "",
                        title = "百度",
                        content = "https://www.baidu.com/",
                        createTime = System.currentTimeMillis()
                    ),
                    arrayListOf(LabelQuote(labelName = "电影"))
                ),
                CollectionComplete(
                    Collection(
                        type = CollectionType.Text,
                        original = "",
                        title = "标题",
                        content = "预览",
                        createTime = System.currentTimeMillis()
                    ),
                    arrayListOf(LabelQuote(labelName = "歌曲"))
                )
            )
        } else {
            listOf()
        }
    )
    val collectionsLoading = _collectionsLoading.asStateFlow()
    val collections = _collections.asStateFlow()

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