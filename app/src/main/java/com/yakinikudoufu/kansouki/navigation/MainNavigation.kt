package com.yakinikudoufu.kansouki.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yakinikudoufu.kansouki.R
import com.yakinikudoufu.kansouki.views.MainView
import com.yakinikudoufu.kansouki.views.SecondView
import com.yakinikudoufu.kansouki.views.SettingView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var homeFlg by rememberSaveable { mutableStateOf(true) }
    var secondFlg by rememberSaveable { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    label = { Text(text = "home") },
                    selected = homeFlg,
                    icon = {
                        if(homeFlg){
                            Icon(Icons.Filled.Home, contentDescription = "Localized description")
                        }else{
                            Icon(Icons.Outlined.Home, contentDescription = "Localized description")
                        }
                    },
                    onClick = {
                        scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                        navController.navigate("home")
                        homeFlg = true
                        secondFlg = false
                    }
                )
                NavigationDrawerItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    label = { Text(text = "second") },
                    selected = secondFlg,
                    icon = {
                        if(secondFlg){
                            Icon(Icons.Filled.Home, contentDescription = "Localized description")
                        }else{
                            Icon(Icons.Outlined.Home, contentDescription = "Localized description")
                        }
                    },
                    onClick = {
                        scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                        navController.navigate("second")
                        homeFlg = false
                        secondFlg = true
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(
                        text = stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    ) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                        }) {
                            Icon(imageVector = Icons.Filled.Menu,  contentDescription = "Localized description")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            homeFlg = false
                            secondFlg = false
                            navController.navigate("settings")
                        }) {
                            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings Button")
                        }
                    })
            },
            content = {
                val paddingValue = it
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        // ホーム画面のコンポーザーを設定
                        MainView(paddingValues = paddingValue)
                    }
                    composable("second") {
                        // 設定画面のコンポーザーを設定
                        SecondView(paddingValues = paddingValue)
                    }
                    composable("settings") {
                        // 設定画面のコンポーザーを設定
                        SettingView(paddingValues = paddingValue)
                    }
                }
            },
        )
    }
}