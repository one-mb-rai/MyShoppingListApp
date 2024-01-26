package com.onemb.myshoppinglistapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.onemb.myshoppinglistapp.database.ShoppingListEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShoppingListApp(
    innerPadding: PaddingValues,
    showDialog: MutableState<Boolean>,
    dateSelected: MutableState<String>,
    viewModel: ShoppingListViewModel
) {

    var shoppingListItems = viewModel.shoppingList.collectAsState(initial = emptyList())

    var itemName = remember {
        mutableStateOf("")
    }
    var quantity = remember {
        mutableStateOf("")
    }
    val uId = remember { mutableStateOf("null") }
    val editMode = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center
    ) {
        if(shoppingListItems.value.isEmpty()) {
            Text(text = "Nothing to display for:- ${dateSelected.value}",
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
            )
        } else {
            Text(text = "Items for:- ${dateSelected.value}",
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(shoppingListItems.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(end = 15.dp),
                        text = it.id.toString() + "."
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ReusableCard(it, viewModel, showDialog, itemName, quantity, dateSelected, uId, editMode)
                    }
                }
            }
        }
        AlertDialogApp(
            showDialog,
            itemName,
            quantity,
            shoppingListItems,
            dateSelected,
            viewModel,
            uId.value,
            editMode.value
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReusableCard(
    it: ShoppingListEntity,
    viewModel: ShoppingListViewModel,
    showDialog: MutableState<Boolean>,
    itemName:MutableState<String>,
    quantity:MutableState<String>,
    dateEdited:MutableState<String>,
    uId: MutableState<String>,
    editMode: MutableState<Boolean>,
) {
    val context = LocalContext.current
    val data = it;
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp).shadow(4.dp, RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            val decoration = remember{
                mutableStateOf(TextDecoration.None)
            }
            if(it.markCompleted) {
                decoration.value = TextDecoration.LineThrough
            }
            Text(
                text = "Name: "+it.itemName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                textDecoration = decoration.value
            )

            Text(
                text = "Quantity: "+ it.itemQuantity,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                textDecoration = decoration.value
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    if(!it.markCompleted) {
                        itemName.value = it.itemName
                        quantity.value = it.itemQuantity
                        dateEdited.value = LocalDate.now().toString()
                        uId.value = it.id.toString()
                        editMode.value = true
                        showDialog.value = true
                    }
                }) {
                    val imageVector = remember{
                        mutableStateOf(Icons.Filled.Create)
                    }
                    if(it.markCompleted) {
                        imageVector.value = Icons.Outlined.Create
                    }
                    Icon(
                        imageVector = imageVector.value,
                        contentDescription = "edit Item"
                    )
                }
                IconButton(onClick = {
                    if(!it.markCompleted) {
                        context.let {
                            CoroutineScope(Dispatchers.Default).launch {
                                viewModel.deleteShoppingItem(data)
                            }
                        }
                    }
                }) {
                    val imageVector = remember{
                        mutableStateOf(Icons.Filled.Delete)
                    }
                    if(it.markCompleted) {
                        imageVector.value = Icons.Outlined.Delete
                    }
                    Icon(
                        imageVector = imageVector.value,
                        contentDescription = "delete Item"
                    )
                }
                IconButton(onClick = {
                    if(!it.markCompleted) {
                        context.let {
                            CoroutineScope(Dispatchers.Default).launch {
                                val newItem = ShoppingListEntity(
                                    id = data.id,
                                    itemName = data.itemName,
                                    itemQuantity = data.itemQuantity,
                                    itemEditedOn = LocalDate.now().toString(),
                                    markCompleted = true
                                )
                                viewModel.updateShoppingItem(newItem)
                            }
                        }
                    } else {
                        context.let {
                            CoroutineScope(Dispatchers.Default).launch {
                                val newItem = ShoppingListEntity(
                                    id = data.id,
                                    itemName = data.itemName,
                                    itemQuantity = data.itemQuantity,
                                    itemEditedOn = LocalDate.now().toString(),
                                    markCompleted = false
                                )
                                viewModel.updateShoppingItem(newItem)
                            }
                        }
                    }
                }) {
                    val imageVector = remember{
                        mutableStateOf(Icons.Filled.Done)
                    }
                    if(it.markCompleted) {
                        imageVector.value = Icons.Outlined.Refresh
                    }
                    Icon(
                        imageVector = imageVector.value,
                        contentDescription = "Bought Item"
                    )
                }
            }
        }
    }
}
