package com.lijie.jiancang.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.viewmodel.LocalViewModel


val LocalViewModel = staticCompositionLocalOf {
    LocalViewModel()
}

@ExperimentalAnimationApi
@Composable
fun Navigation(
    localViewModel: LocalViewModel = viewModel(),
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String,
    collectionContent: String = "",
    collectionType: CollectionType = CollectionType.Text,
) {
    CompositionLocalProvider(LocalViewModel provides localViewModel) {
        var arguments = bundleOf()
        AnimatedNavHost(navController = navController, startDestination = startDestination,
            enterTransition = { _, _ ->
                slideInHorizontally(initialOffsetX = { 1000 })
            },
            exitTransition = { _, _ ->
                slideOutHorizontally(targetOffsetX = { -1000 })
            },
            popEnterTransition = { _, _ ->
                slideInHorizontally(initialOffsetX = { -1000 })
            },
            popExitTransition = { _, _ ->
                slideOutHorizontally(targetOffsetX = { 1000 })
            }) {
            composable(route = Screen.AddCollectionScreen.route) {
                AddCollectionScreen(content = collectionContent, type = collectionType)
            }
            composable(route = Screen.MainScreen.route) {
                MainScreen()
            }
        }
    }
}