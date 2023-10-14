package com.yakinikudoufu.kansouki.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.yakinikudoufu.kansouki.menu.TitleBer

@Composable
fun SettingView(navController: NavHostController) {
    TitleBer(isBack = true, navController = navController) { paddingValue ->
        Box(Modifier.padding(paddingValue)) {
            Text(text = "setting") //TODO アプリ設定画面
        }
    }
}