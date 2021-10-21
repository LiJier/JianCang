package com.lijie.jiancang.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.view.WindowCompat
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.screen.Navigation
import com.lijie.jiancang.screen.Screen

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val (content, type) = when (intent.action) {
            Intent.ACTION_PROCESS_TEXT -> {
                Pair(
                    intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString(),
                    CollectionType.Text
                )
            }
            Intent.ACTION_SEND -> {
                when (intent.type) {
                    "text/plain" -> {
                        Pair(intent.getStringExtra(Intent.EXTRA_TEXT) ?: "", CollectionType.Text)
                    }
                    "image/*" -> {
                        Pair(
                            intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM).toString(),
                            CollectionType.Image
                        )
                    }
                    else -> {
                        Pair("", CollectionType.Text)
                    }
                }
            }
            else -> {
                Pair("", CollectionType.Text)
            }
        }
        if (content.isEmpty()) {
            setContent {
                Navigation(startDestination = Screen.MainScreen.route)
            }
        } else {
            setContent {
                Navigation(
                    startDestination = Screen.AddCollectionScreen.route,
                    collectionContent = content,
                    collectionType = type
                )
            }
        }
    }

}

