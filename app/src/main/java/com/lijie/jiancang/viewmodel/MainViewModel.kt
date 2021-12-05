package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.ext.launch
import com.lijie.jiancang.repository.IRepository
import com.lijie.jiancang.repository.ResFlow
import com.lijie.jiancang.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    val collectionsRes = ResFlow<List<CollectionComplete>>(listOf())
    val deleteCollectionRes = ResFlow(false)

    fun queryCollections() {
        launch(collectionsRes) {
            repository.getAllCollection()
        }
    }

    fun deleteCollection(collectionComplete: CollectionComplete) {
        launch(deleteCollectionRes) {
            val deleteResult = repository.deleteCollection(collectionComplete)
            if (deleteResult is Result.Success) {
                queryCollections()
            }
            deleteResult
        }
    }

}