package com.onemb.myshoppinglistapp.database

import android.content.Context


class AppContainer(private val context: Context) {

    private val database:ShoppingListDb by lazy {
        ShoppingListDb.getInstance(context)
    }

    private val shoppingListDao: ShoppingListDao by lazy {
        database.shoppingListDao()
    }

    val shoppingListRepo: ShoppinglistRepo by lazy {
        OfflineShoppingRepo(shoppingListDao)
    }
}