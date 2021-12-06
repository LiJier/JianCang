package com.lijie.jiancang.viewmodel

import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.repository.IRepository
import com.lijie.jiancang.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(repository: IRepository) : BaseViewModel(repository) {

    val collectionsRes = ResFlow<List<CollectionComplete>>(listOf())
    val deleteCollectionRes = ResFlow(false)

    fun queryCollections() {
        launch(collectionsRes) {
            getAllCollection()
        }
    }

    fun deleteCollection(collectionComplete: CollectionComplete) {
        launch(deleteCollectionRes) {
            val deleteResult = deleteCollection(collectionComplete)
            if (deleteResult is Result.Success) {
                queryCollections()
            }
            deleteResult
        }
    }

}