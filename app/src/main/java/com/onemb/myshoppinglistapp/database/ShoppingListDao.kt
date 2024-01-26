package com.onemb.myshoppinglistapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM ShoppingListEntity")
    fun getAll(): Flow<List<ShoppingListEntity>>

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User
//
    @Insert
    suspend fun insert(shoppingRow: ShoppingListEntity)
//
//    @Delete
//    fun delete(user: User)
}