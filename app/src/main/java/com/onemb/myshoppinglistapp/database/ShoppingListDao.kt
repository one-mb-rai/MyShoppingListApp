package com.onemb.myshoppinglistapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM ShoppingListEntity")
    fun getAll(): Flow<List<ShoppingListEntity>>

    @Update
    suspend fun update(shoppingRow: ShoppingListEntity)

    @Insert
    suspend fun insert(shoppingRow: ShoppingListEntity)

    @Delete
    suspend fun delete(shoppingRow: ShoppingListEntity)
}