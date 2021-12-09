package com.lijie.jiancang.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.ExperimentalAnimatedInsets
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.db.entity.CollectionType

@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalAnimatedInsets
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalUnitApi
@ExperimentalCoilApi
@ExperimentalAnimationApi
@Composable
fun Navigation(
    startDestination: String,
    collectionContent: String = "",
    collectionType: CollectionType = CollectionType.Text,
) {
    val navController = rememberAnimatedNavController()
    val arguments = bundleOf()
    Screen(topBar = {}) {
        AnimatedNavHost(navController = navController,
            startDestination = startDestination,
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
            composable(route = AddCollectionScreen.route) {
                AddCollectionScreen(content = collectionContent, type = collectionType)
            }
            composable(route = MainNavigation.route) {
                MainNavigation({
                    navController.navigate(it)
                }, {
                    arguments.putParcelable(COLLECTION_COMPLETE_KEY, it)
                    navController.navigate(CollectionDetailScreen.route)
                })
            }
            composable(route = CollectionDetailScreen.route) {
                arguments.getParcelable<CollectionComplete>(
                    COLLECTION_COMPLETE_KEY
                )?.let {
                    CollectionDetailScreen(
                        hiltViewModel(), it
                    )
                }
            }
            composable(route = LabelManagerScreen.route) {
                LabelManagerScreen(hiltViewModel())
            }
        }
    }
}

const val COLLECTION_COMPLETE_KEY = "collection_complete_key"