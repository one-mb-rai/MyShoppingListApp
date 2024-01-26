package com.onemb.myshoppinglistapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.onemb.myshoppinglistapp.database.ShoppingListEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertDialogApp(
    showDialog: MutableState<Boolean>,
    itemName: MutableState<String>,
    quantity: MutableState<String>,
    list: State<List<ShoppingListEntity>>,
    dateSelected: MutableState<String>,
    viewModel: ShoppingListViewModel,
    id: String,
    editMode: Boolean,
) {
    val context = LocalContext.current
    val modifier =  Modifier.padding(8.dp)
    if(showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        if(itemName.value != "") {
                            val uId : Int
                            if(id != "null") {
                                uId = id.toInt()
                            } else {
                                uId = list.value.size + 1
                            }
                            val newItem =  ShoppingListEntity (
                                id = uId,
                                itemName = itemName.value,
                                itemQuantity = quantity.value,
                                itemEditedOn = LocalDate.now().toString(),
                                markCompleted = false
                            )
                            context.let {
                                CoroutineScope(Dispatchers.Default).launch {
                                    if(editMode) {
                                        viewModel.updateShoppingItem(newItem)
                                    } else {
                                        viewModel.insertShoppingItem(newItem)
                                    }

                                }
                            }
                            itemName.value = ""
                            quantity.value = ""
                            showDialog.value = false
                        }
                    }) {
                        if(editMode) {
                            Text(text = "Edit")
                        } else {
                            Text(text = "Add")
                        }
                    }
                    Button(onClick = {
                        itemName.value = ""
                        quantity.value = ""
                        showDialog.value = false
                    }) {
                        Text(text = "Cancel")
                    }
                }
            },
            title = { Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName.value,
                        onValueChange = {itemName.value = it},
                        modifier,
                        singleLine = true,
                        placeholder = { Text(text = "Item Name")}
                    )
                    OutlinedTextField(
                        value = quantity.value,
                        onValueChange = {quantity.value = it},
                        modifier,
                        singleLine = true,
                        placeholder = { Text(text = "Item Quantity")}
                    )
                }
            })
    }
}
