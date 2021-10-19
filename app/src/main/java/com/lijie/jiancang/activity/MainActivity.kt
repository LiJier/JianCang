package com.lijie.jiancang.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.view.WindowCompat
import com.lijie.jiancang.screen.Navigation
import com.lijie.jiancang.screen.Screen

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val content = when (intent.action) {
            Intent.ACTION_PROCESS_TEXT -> {
                intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
            }
            Intent.ACTION_SEND -> {
                when (intent.type) {
                    "text/plain" -> {
                        intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
                    }
                    else -> {
                        ""
                    }
                }
            }
            else -> {
                ""
            }
        }
        if (content.isEmpty()) {
            setContent {
                Navigation(startDestination = Screen.MainScreen.route)
            }
        } else {
            setContent {
                Navigation(shareContent = content)
            }
        }
    }

}

