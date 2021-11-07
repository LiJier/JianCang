package com.lijie.jiancang.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijie.jiancang.db.AppDatabase
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel : ViewModel() {

    private val _collections = MutableStateFlow<List<CollectionComplete>>(listOf())
    val collections = _collections.asStateFlow()

    fun setCollections(collectionCompletes: List<CollectionComplete>) {
        _collections.value = collectionCompletes
    }

    fun queryCollections() {
        viewModelScope.launch(Dispatchers.IO) {
            val collectionDao = AppDatabase.db.collectionDao()
            setCollections(collectionDao.queryCollections())
        }
    }

    fun deleteCollection(collectionComplete: CollectionComplete) {
        viewModelScope.launch(Dispatchers.IO) {
            if (collectionComplete.collection.type == CollectionType.Image) {
                File(collectionComplete.collection.content).delete()
            }
            AppDatabase.db.collectionDao().delete(collectionComplete.collection)
            AppDatabase.db.labelQuoteDao().delete(collectionComplete.labelQuote)
            queryCollections()
        }
    }

}