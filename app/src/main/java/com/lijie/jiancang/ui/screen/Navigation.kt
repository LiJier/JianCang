package com.lijie.jiancang.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


//val LocalViewModel = staticCompositionLocalOf {
//    LocalViewModel()
//}

@ExperimentalAnimationApi
@Composable
fun Navigation(
//    localViewModel: LocalViewModel = viewModel(),
    navController: NavHostController = rememberAnimatedNavController()
) {
    CompositionLocalProvider(
//        LocalViewModel provides localViewModel
    ) {
        var arguments = bundleOf()
        AnimatedNavHost(navController = navController, startDestination = "",
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

        }
    }
}