package com.yakinikudoufu.kansouki.menu

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.navigation.NavController
import com.yakinikudoufu.kansouki.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(
    navController: NavController,
    isSelect: String,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var homeFlg by rememberSaveable { mutableStateOf(false) }
    var secondFlg by rememberSaveable { mutableStateOf(false) }
    when(isSelect){
        "home" -> homeFlg = true
        "second" -> secondFlg = true
    }

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
                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings Button")
                        }
                    }
                )
            },
            content = {
                content(it)
            },
        )
    }
}