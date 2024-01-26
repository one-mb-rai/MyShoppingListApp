package com.onemb.myshoppinglistapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myshoppinglistapp.ui.theme.MyShoppingListAppTheme
import com.onemb.myshoppinglistapp.database.AppContainer
import com.onemb.myshoppinglistapp.database.ShoppingListEntity
import com.onemb.myshoppinglistapp.database.ShoppinglistRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    lateinit var appContainer: AppContainer private set
    private val shoppingListRepo: ShoppinglistRepo by lazy {
        appContainer.shoppingListRepo
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = AppContainer(applicationContext)

        setContent {
            MyShoppingListAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CenterAlignedTopAppBarExample(appContainer, shoppingListRepo)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopAppBarExample(appContainer: AppContainer, shoppingListRepo: ShoppinglistRepo) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val dateSelected = remember {mutableStateOf(LocalDate.now().toString())}
    val showDialogForCalender = remember { mutableStateOf(false) }
    var showDialogForAddItem  =  remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "My Shopping List App",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        showDialogForCalender.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Open Calender"
                        )
                    }
                    IconButton(onClick = {
                        showDialogForAddItem.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Item"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        val shoppingListViewModel: ShoppingListViewModel = viewModel(
            factory = ShoppingListViewModelFactory(shoppingListRepo)
        )
        ShoppingListApp(innerPadding, showDialogForAddItem, dateSelected, shoppingListViewModel)
        when {
            showDialogForCalender.value -> {
                AlertDialog(
                    onDismissRequest = {  },
                    confirmButton = { },
                    text = {
                        CalendarView { localDate: LocalDate ->
                            dateSelected.value = localDate.toString()
                            showDialogForCalender.value = false
                        }
                    }
                )
            }
        }
    }
}

class ShoppingListViewModel(private val shoppingListRepo: ShoppinglistRepo) : ViewModel() {
    val shoppingList: Flow<List<ShoppingListEntity>> = shoppingListRepo.getAll()

    // Function to insert a new shopping list item
    fun insertShoppingItem(shoppingItem: ShoppingListEntity) {
        viewModelScope.launch {
            shoppingListRepo.insert(shoppingItem)
        }
    }

    fun deleteShoppingItem(shoppingItem: ShoppingListEntity) {
        viewModelScope.launch {
            shoppingListRepo.delete(shoppingItem)
        }
    }

    fun updateShoppingItem(shoppingItem: ShoppingListEntity) {
        viewModelScope.launch {
            shoppingListRepo.update(shoppingItem)
        }
    }
}

class ShoppingListViewModelFactory(private val shoppingListRepo: ShoppinglistRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            return ShoppingListViewModel(shoppingListRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}