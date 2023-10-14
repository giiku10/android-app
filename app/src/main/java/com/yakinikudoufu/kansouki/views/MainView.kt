package com.yakinikudoufu.kansouki.views

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakinikudoufu.kansouki.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(paddingValues: PaddingValues, navController: NavHostController) {
    Scaffold(
        modifier = Modifier.padding(paddingValues),
        content = {
            MainContent(navController, it)
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
fun MainContent(navController: NavHostController, paddingValues: PaddingValues) {
    Box(Modifier.padding(paddingValues)) {
        var dataList by remember { mutableStateOf<List<String>>(emptyList()) }
        val db = Firebase.firestore
        db.collection("Class")
            .get()
            .addOnSuccessListener { result ->
                dataList = result.documents.mapNotNull { it.getString("name") }
            }
            .addOnFailureListener { exception ->
                Log.w("testLog", "Error getting documents.", exception)
            }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dataList.size) {
                CardItem(navController, dataList[it])
                Log.d("testLog", "MainContent: " + dataList[it])
            }
        }
    }
}

@Composable
fun CardItem(navController: NavHostController, data: String) {
    Card(
        modifier = Modifier
            .padding(8.dp, 8.dp, 8.dp, 0.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("class_item/$data")
            }
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = data,
            fontSize = 25.sp,
            maxLines = 1
        )
    }
}
