package com.yakinikudoufu.kansouki.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SecondView(paddingValues: PaddingValues) {
    Box(Modifier.padding(paddingValues)) {
        Text(text = "Second")
    }
}