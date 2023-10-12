package com.yakinikudoufu.kansouki.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingView(paddingValues: PaddingValues) {
    Box(Modifier.padding(paddingValues)) {
        Text(text = "setting")
    }
}