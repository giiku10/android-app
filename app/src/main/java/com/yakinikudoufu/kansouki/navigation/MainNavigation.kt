package com.yakinikudoufu.kansouki.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yakinikudoufu.kansouki.menu.MainMenu
import com.yakinikudoufu.kansouki.views.MainView
import com.yakinikudoufu.kansouki.views.SecondView
import com.yakinikudoufu.kansouki.views.SettingView

@Composable
fun MainNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainMenu(navController = navController) {
                MainView(paddingValues = it)
            }
        }
        composable("second") {
            MainMenu(navController = navController) {
                SecondView(paddingValues = it)
            }
        }
        composable("settings") {
            SettingView(navController = navController)
        }
    }
}