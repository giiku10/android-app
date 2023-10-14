package com.yakinikudoufu.kansouki.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yakinikudoufu.kansouki.views.ClassItemsView
import com.yakinikudoufu.kansouki.views.MainView
import com.yakinikudoufu.kansouki.views.SettingView

@Composable
fun MainNavigation(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            MainView(navController = navController)
        }
        composable(
            route = "class_item/{id}",
            arguments = listOf( navArgument("id") { type = NavType.StringType } )
        ){arguments ->
            val id = arguments.arguments?.getString("id") ?: ""
            ClassItemsView(navController = navController, id = id)
        }
        composable("settings") {
            SettingView(navController = navController)
        }
    }
}