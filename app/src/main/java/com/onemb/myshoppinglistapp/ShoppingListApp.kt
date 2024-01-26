package com.onemb.myshoppinglistapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onemb.myshoppinglistapp.database.ShoppingListEntity
import com.onemb.myshoppinglistapp.database.ShoppinglistRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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
//                        Text(text = it.itemName, modifier = Modifier.widthIn(min = 48.dp, max = 48.dp))
//                        Text(text = it.itemQuantity, modifier = Modifier.widthIn(min = 48.dp, max = 75.dp))
//                        Row (modifier = Modifier.widthIn(min = 100.dp)) {
//                            OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(end = 5.dp)) {
//                                Text(text = "Edit")
//                            }
//                            OutlinedButton(onClick = { /*TODO*/ }) {
//                                Text(text = "Delete")
//                            }
//                        }
                        ReusableCard(it)
                    }
                }
            }
        }
        AlertDialogApp(showDialog, itemName, quantity, shoppingListItems, dateSelected, viewModel)
    }
}

@Composable
fun ReusableCard(it: ShoppingListEntity) {
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
            // Example content inside the card
            Text(
                text = "Name: "+it.itemName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Quantity: "+ it.itemQuantity,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                }) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "edit Item"
                    )
                }
                IconButton(onClick = {
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "delete Item"
                    )
                }
                IconButton(onClick = {
                }) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Bought Item"
                    )
                }
            }
        }
    }
}