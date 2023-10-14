package com.yakinikudoufu.kansouki.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.yakinikudoufu.kansouki.R
import com.yakinikudoufu.kansouki.menu.TitleBer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassItemsView(navController: NavController, id: String) {
    var isDialog by remember { mutableStateOf(false) }
    var nameList by remember { mutableStateOf<List<String>>(emptyList()) }
    var idList by remember { mutableStateOf<List<String>>(emptyList()) }
    var detailList by remember { mutableStateOf<List<String>>(emptyList()) }
    var checkList by remember { mutableStateOf<List<Boolean>>(emptyList()) }
    var difficulty by remember { mutableStateOf<List<List<Int>>>(emptyList()) }
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("Parts")
            .whereEqualTo("parent", id)
            .orderBy("detail", Query.Direction.DESCENDING).orderBy("name")
            .get()
            .addOnSuccessListener { result ->
                nameList = result.documents.mapNotNull { it.getString("name") }
                detailList = result.documents.mapNotNull { it.getString("detail") }
                checkList = result.documents.mapNotNull { it.getBoolean("check") }
                idList = result.documents.mapNotNull { it.id }
                difficulty = result.documents.mapNotNull { document ->
                    val difficultArray = document.get("difficulty") as? List<Int>
                    difficultArray ?: emptyList()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("testLog", "Error getting documents.", exception)
            }
    }
    TitleBer(
        isBack = true,
        navController = navController,
        content = {paddingValue ->
            Scaffold(
                modifier = Modifier.padding(paddingValue),
                content = { it ->
                    MainContent(
                        paddingValue = it,
                        navController = navController,
                        detailList = detailList,
                        nameList = nameList,
                        idList = idList,
                        checkList = checkList,
                        difficulty = difficulty
                    )
                    if (isDialog){
                        AddPartsDialog(
                            id = id,
                            onDismissRequest = { isDialog =false },
                            onConfirmation = {
                                isDialog =false
                                val db = Firebase.firestore
                                db.collection("Parts")
                                    .whereEqualTo("parent", id)
                                    .orderBy("detail", Query.Direction.DESCENDING).orderBy("name")
                                    .get()
                                    .addOnSuccessListener { result ->
                                        nameList = result.documents.mapNotNull { it.getString("name") }
                                        detailList = result.documents.mapNotNull { it.getString("detail") }
                                        checkList = result.documents.mapNotNull { it.getBoolean("check") }
                                        idList = result.documents.mapNotNull { it.id }
                                        difficulty = result.documents.mapNotNull { document ->
                                            val difficultArray = document.get("difficulty") as? List<*>
                                            difficultArray?.mapNotNull { it as? Int } ?: emptyList()
                                        }
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
                        text = { Text(stringResource(R.string.add_parts)) },
                        onClick = { isDialog = true }
                    )
                }
            )
        }
    )
}

@Composable
fun MainContent(
    paddingValue:PaddingValues,
    navController:NavController,
    detailList:List<String>,
    nameList:List<String>,
    idList:List<String>,
    checkList:List<Boolean>,
    difficulty:List<List<Int>>
){
    Box(modifier = Modifier
        .padding(paddingValue)
        .fillMaxHeight()){
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(detailList.size) {
                when(detailList[it]){
                    "folder" -> {
                        PartsCardItem(
                            navController = navController,
                            data = nameList[it],
                            id = idList[it],
                        )
                    }
                    "file" -> {
                        QuestionCardItem(
                            data = nameList[it],
                            checkList = checkList[it],
                            id = idList[it],
                            difficulty = difficulty[it]
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PartsCardItem(navController: NavController, data: String, id: String) {
    Card(
        modifier = Modifier
            .padding(8.dp, 8.dp, 8.dp, 0.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("class_item/$id") }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.Folder, contentDescription = "Folder Icon", modifier = Modifier.width(52.dp))
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = data,
                fontSize = 25.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(modifier = Modifier
                .fillMaxHeight()
                .padding(end = 8.dp), imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "next page")
        }
    }
}
@Composable
fun QuestionCardItem(data: String,checkList:Boolean, id: String,difficulty:List<Int>) {
    var isCheck by remember{ mutableStateOf(checkList) }
    Card(
        modifier = Modifier
            .padding(8.dp, 8.dp, 8.dp, 0.dp)
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                modifier = Modifier.width(52.dp),
                checked = isCheck,
                onCheckedChange = {
                    isCheck = !isCheck
                    val db = Firebase.firestore
                    db.collection("Parts").document(id).update("check", isCheck)
                }
            )
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = data,
                fontSize = 25.sp,
                maxLines = 1
            )
        }
        if (difficulty.isNotEmpty()){
            val sum = difficulty.sum()
            val average = sum.toFloat() / difficulty.size
            Slider(value = average/100, onValueChange = {}, enabled = false, modifier = Modifier.fillMaxWidth())
        }
        Log.d("TAG", "QuestionCardItem: "+difficulty.size)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPartsDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    id: String
) {
    var partsName by remember { mutableStateOf("") }
    val radioOptions = listOf("問題群", "問題")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    AlertDialog(
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        title = { Text(stringResource(R.string.add_parts)) },
        text = {
            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
                TextField(
                    value = partsName,
                    onValueChange = {partsName = it},
                    label = { Text(text = "問題名")},
                )
            }
        },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    val detail = if (selectedOption == "問題群") "folder" else "file"
                    val data = hashMapOf(
                        "check" to false,
                        "detail" to detail,
                        "difficulty" to emptyList<Int>(),
                        "name" to partsName,
                        "parent" to id,
                    )
                    val db = Firebase.firestore
                    db.collection("Parts").add(data)
                    onConfirmation()
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