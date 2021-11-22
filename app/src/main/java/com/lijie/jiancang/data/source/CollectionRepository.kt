package com.lijie.jiancang.data.source

import com.lijie.jiancang.data.db.entity.CollectionComplete

interface CollectionRepository {

    suspend fun getAllCollection(): List<CollectionComplete>

}