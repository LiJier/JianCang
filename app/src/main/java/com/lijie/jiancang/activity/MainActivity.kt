package com.lijie.jiancang.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.view.WindowCompat
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.lijie.jiancang.data.db.entity.CollectionType
import com.lijie.jiancang.screen.AddCollectionScreen
import com.lijie.jiancang.screen.MainScreen
import com.lijie.jiancang.screen.Navigation
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalAnimatedInsets
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val (content, type) = when (intent.action) {
            Intent.ACTION_PROCESS_TEXT -> {
                val s = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
                Pair(s, CollectionType.Text)
            }
            Intent.ACTION_SEND -> {
                when (intent.type) {
                    "text/plain" -> {
                        val s = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
                        Pair(s, CollectionType.Text)
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
            Intent.ACTION_VIEW -> {
                if (intent.categories.contains(Intent.CATEGORY_BROWSABLE)) {
                    val s = intent.dataString ?: ""
                    Pair(s, CollectionType.Text)
                } else {
                    Pair("", CollectionType.Text)
                }
            }
            else -> {
                Pair("", CollectionType.Text)
            }
        }
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .request { allGranted, _, _ ->
                if (allGranted) {
                    if (content.isEmpty()) {
                        setContent {
                            Navigation(startDestination = MainScreen.route)
                        }
                    } else {
                        setContent {
                            Navigation(
                                startDestination = AddCollectionScreen.route,
                                collectionContent = content,
                                collectionType = type
                            )
                        }
                    }
                }
            }
    }

}

