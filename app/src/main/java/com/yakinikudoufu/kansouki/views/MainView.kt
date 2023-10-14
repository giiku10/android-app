package com.yakinikudoufu.kansouki.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.yakinikudoufu.kansouki.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(paddingValues: PaddingValues) {
    Scaffold(
        modifier = Modifier.padding(paddingValues),
        content = {
            MainContent(it)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.Add,"Add Button") },
                text = { Text(stringResource(R.string.add_class)) },
                onClick = {
                    // TODO
                }
            )
        }
    )
}
@Composable
fun MainContent(paddingValues: PaddingValues) {
    Box(Modifier.padding(paddingValues)) {
        Text(text = "First")
    }
}
