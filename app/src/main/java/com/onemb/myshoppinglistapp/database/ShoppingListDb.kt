package com.onemb.myshoppinglistapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShoppingListEntity::class], version = 1)
abstract class ShoppingListDb : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

    companion object {
        @Volatile
        private var INSTANCE: ShoppingListDb? = null

        fun getInstance(context: Context): ShoppingListDb {
            return INSTANCE ?: synchronized(this) {
                 Room.databaseBuilder(
                    context,
                    ShoppingListDb::class.java,
                    "ShoppingListDB"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}