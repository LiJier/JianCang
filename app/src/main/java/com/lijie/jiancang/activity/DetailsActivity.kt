package com.lijie.jiancang.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.view.WindowCompat
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.screen.COLLECTION_COMPLETE_KEY
import com.lijie.jiancang.screen.CollectionDetailScreen

@ExperimentalUnitApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
class DetailsActivity : AppCompatActivity() {

    private val collectionComplete by lazy {
        intent.getParcelableExtra<CollectionComplete>(COLLECTION_COMPLETE_KEY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            collectionComplete?.let { CollectionDetailScreen(collectionComplete = it) }
        }
    }

}