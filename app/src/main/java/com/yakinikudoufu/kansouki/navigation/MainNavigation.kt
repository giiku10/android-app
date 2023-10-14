package com.yakinikudoufu.kansouki.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yakinikudoufu.kansouki.menu.MainMenu
import com.yakinikudoufu.kansouki.views.ClassItems
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
            MainMenu(navController = navController, isSelect = "home"){ paddingValue ->
                MainView(paddingValues = paddingValue, navController = navController)
            }
        }
        composable("second") {
            MainMenu(navController = navController, isSelect = "second"){ paddingValue ->
                SecondView(paddingValues = paddingValue)
            }
        }
        composable(
            route = "class_item/{data}",
            arguments = listOf( navArgument("data") { type = NavType.StringType } )
        ){arguments ->
            val data = arguments.arguments?.getString("data") ?: ""
            ClassItems(data)
        }
        composable("settings") {
            SettingView(navController = navController)
        }
    }
}