package com.lijie.jiancang

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

class MainActivity : ComponentActivity() {

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
    }

    @Composable
    fun Greeting() {
        val name by remember {
            mutableStateOf(content)
        }
        Text(text = "Hello $name!")
    }

}

