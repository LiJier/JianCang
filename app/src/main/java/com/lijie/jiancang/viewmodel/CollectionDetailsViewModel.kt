package com.lijie.jiancang.viewmodel

import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionDetailsViewModel @Inject constructor(repository: IRepository) :
    BaseViewModel(repository) {

    private lateinit var collectionComplete: CollectionComplete
    private var mdContent: String = ""
    val savedRes = ResFlow(false)

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
        launch(savedRes) {
            updateCollection(collectionComplete, mdContent)
        }
    }

}