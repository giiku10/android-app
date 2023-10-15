package com.yakinikudoufu.kansouki.views

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakinikudoufu.kansouki.R
import com.yakinikudoufu.kansouki.menu.TitleBer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(navController: NavHostController) {
    var isDialog by remember { mutableStateOf(false) }
    var dataList by remember { mutableStateOf<List<String>>(emptyList()) }
    var idList by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("Class").orderBy("name")
            .get()
            .addOnSuccessListener { result ->
                dataList = result.documents.mapNotNull { it.getString("name") }
                idList = result.documents.mapNotNull { it.id }
                Log.d("test", "MainView: run")
            }
            .addOnFailureListener { exception ->
                Log.w("testLog", "Error getting documents.", exception)
            }
    }
    TitleBer(isBack = false, navController = navController) {paddingValues ->
        Scaffold(
            modifier = Modifier.padding(paddingValues),
            content = {
                MainContent(navController, it,dataList,idList)
                if (isDialog){
                    AddClassDialog(
                        onDismissRequest = { isDialog =false },
                        onConfirmation = {
                            isDialog =false
                            val db = Firebase.firestore
                            db.collection("Class").orderBy("name")
                                .get()
                                .addOnSuccessListener { result ->
                                    dataList = result.documents.mapNotNull { it.getString("name") }
                                    idList = result.documents.mapNotNull { it.id }
                                    Log.d("test", "MainView: run")
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("testLog", "Error getting documents.", exception)
                                }
                        },
                    )
                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    icon = { Icon(Icons.Filled.Add,"Add Button") },
                    text = { Text(stringResource(R.string.add_class)) },
                    onClick = { isDialog = true }
                )
            }
        )
    }
}

@Composable
fun MainContent(
    navController: NavHostController,
    paddingValues: PaddingValues,
    dataList: List<String>,
    idList: List<String>
) {
    Box(Modifier.padding(paddingValues)) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dataList.size) {
                ClassCardItem(navController, dataList[it], idList[it])
            }
        }
    }
}

@Composable
fun ClassCardItem(navController: NavHostController, data: String, id: String) {
    Card(
        modifier = Modifier
            .padding(8.dp, 8.dp, 8.dp, 0.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("class_item/$id")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClassDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    var className by remember { mutableStateOf("") }
    var  errorText by remember { mutableStateOf("") }
    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        title = { Text(stringResource(R.string.add_class)) },
        text = {
            TextField(
                value = className,
                onValueChange = {className = it; errorText = ""},
                label = { Text(text = "授業名")},
                maxLines = 1,
                singleLine = true,
                isError = errorText.isNotBlank(),
                supportingText = { Text(text = errorText)}
            )
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    if (className != ""){
                        val data = hashMapOf("name" to className)
                        val db = Firebase.firestore
                        db.collection("Class").add(data)
                        onConfirmation()
                    }else {
                        errorText = "授業名を入力してください"
                    }
                }
            ) {
                Text("作成")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("キャンセル")
            }
        }
    )
}