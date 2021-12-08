package com.lijie.jiancang.ui.screen

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lijie.jiancang.db.entity.CollectionComplete
import com.lijie.jiancang.ui.compose.TopAppBar

object MainNavigation : Screen("main_navigation")

@ExperimentalPagerApi
@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun MainNavigation(
    onDrawerItemClick: (String) -> Unit,
    onMainItemClick: (CollectionComplete) -> Unit
) {
    val back = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var drawerStateValue by remember { mutableStateOf(drawerState.currentValue) }
    val pagerState = rememberPagerState()
    var currentPage by remember { mutableStateOf(0) }
    val backCallBack by remember {
        mutableStateOf<OnBackPressedCallback>(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerStateValue == DrawerValue.Open) {
                    drawerStateValue = DrawerValue.Closed
                }
            }

        })
    }
    LaunchedEffect(Unit) {
        back?.addCallback(lifecycleOwner, backCallBack)
    }
    LaunchedEffect(drawerState.currentValue) {
        drawerStateValue = drawerState.currentValue
    }
    LaunchedEffect(drawerStateValue) {
        if (drawerStateValue == DrawerValue.Closed) {
            backCallBack.isEnabled = false
            drawerState.close()
        } else {
            backCallBack.isEnabled = true
            drawerState.open()
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }
    LaunchedEffect(currentPage) {
        pagerState.animateScrollToPage(currentPage)
    }
    Screen(topBar = {
        TopAppBar(
            title = { Text(text = "简藏") }
        )
    }, drawerContent = {
        DrawerContent {
            drawerStateValue = DrawerValue.Closed
            onDrawerItemClick(it)
        }
    }, scaffoldState = rememberScaffoldState(drawerState), bottomBar = {
        MainBottomBar(pagerState.currentPage) {
            currentPage = it
        }
    }) {
        HorizontalPager(2, state = pagerState, contentPadding = it) { page ->
            when (page) {
                0 -> {
                    MainScreen(onItemClick = { it1 ->
                        onMainItemClick(it1)
                    })
                }
                1 -> {
                    LabelCollectionScreen()
                }
            }
        }
    }
}

@Composable
fun MainBottomBar(selected: Int, onSelected: (Int) -> Unit) {
    BottomAppBar {
        IconButton(modifier = Modifier.weight(1f), onClick = {
            onSelected(0)
        }) {
            Icon(
                Icons.Default.Home,
                contentDescription = "",
                tint = if (selected == 0) Color.Red else Color.LightGray
            )
        }
        IconButton(modifier = Modifier.weight(1f), onClick = {
            onSelected(1)
        }) {
            Icon(
                Icons.Default.Place,
                contentDescription = "",
                tint = if (selected == 1) Color.Red else Color.LightGray
            )
        }
    }
}

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Preview
@Composable
fun MainNavigationPreview() {
    MainBottomBar(0) {}
}