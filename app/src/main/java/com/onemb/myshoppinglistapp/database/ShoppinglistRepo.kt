package com.onemb.myshoppinglistapp.database

import kotlinx.coroutines.flow.Flow

interface ShoppinglistRepo {
    fun getAll(): Flow<List<ShoppingListEntity>>

    suspend fun insert(shoppingRow: ShoppingListEntity)
}