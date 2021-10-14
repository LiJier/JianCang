package com.lijie.jiancang

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.lijie.jiancang.ui.theme.JianCangTheme

class AddJCActivity : ComponentActivity() {

    private var content: String = "Android"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JianCangTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
        when (intent.action) {
            Intent.ACTION_PROCESS_TEXT -> {
                content = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
            }
            Intent.ACTION_SEND -> {
                when (intent.type) {
                    "text/plain" -> {
                        content = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting() {
        val name by remember {
            mutableStateOf(content)
        }
        Text(text = "Hello $name!")
    }

}