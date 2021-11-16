package com.lijie.jiancang.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType
import com.lijie.jiancang.viewmodel.LocalViewModel


val LocalViewModel = staticCompositionLocalOf {
    LocalViewModel()
}

@ExperimentalAnimatedInsets
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalUnitApi
@ExperimentalCoilApi
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
        val arguments = bundleOf()
        AnimatedNavHost(navController = navController, startDestination = startDestination,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 })
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 })
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 })
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 })
            }) {
            composable(route = Screen.AddCollectionScreen.route) {
                AddCollectionScreen(content = collectionContent, type = collectionType)
            }
            composable(route = Screen.MainScreen.route) {
                MainScreen {
                    arguments.putParcelable(COLLECTION_COMPLETE_KEY, it)
                    navController.navigate(Screen.CollectionDetailScreen.route)
                }
            }
            composable(route = Screen.CollectionDetailScreen.route) {
                arguments.getParcelable<CollectionComplete>(
                    COLLECTION_COMPLETE_KEY
                )?.let {
                    CollectionDetailScreen(
                        collectionComplete = it
                    )
                }
            }
        }
    }
}

const val COLLECTION_COMPLETE_KEY = "collection_complete_key"